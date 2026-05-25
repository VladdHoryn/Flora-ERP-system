package org.example.config;

import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter implements WebFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;

    // 🔥 METRICS
    private Counter allowedRequests;
    private Counter blockedRequests;
    private Counter totalRequests;
    private Timer latencyTimer;

    public RateLimitFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        log.warn("RateLimitFilter INIT");

        allowedRequests = meterRegistry.counter("rate_limit.requests.allowed");
        blockedRequests = meterRegistry.counter("rate_limit.requests.blocked");
        totalRequests = meterRegistry.counter("rate_limit.requests.total");

        latencyTimer = meterRegistry.timer("rate_limit.filter.latency");
    }

    private Bucket createBucket(String key) {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(10)
                        .refillGreedy(5, Duration.ofSeconds(1))
                )
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.defer(() -> {

            Timer.Sample sample = Timer.start(meterRegistry);

            totalRequests.increment(); // 📊 throughput

            return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .flatMap(auth -> applyRateLimit(exchange, chain, auth))
                    .switchIfEmpty(applyRateLimit(exchange, chain, null))
                    .doOnTerminate(() -> sample.stop(latencyTimer)); // ⏱ latency
        });
    }

    private Mono<Void> applyRateLimit(ServerWebExchange exchange,
                                      WebFilterChain chain,
                                      Authentication authentication) {

        String path = exchange.getRequest().getURI().getPath();

        // тільки для потрібного endpoint
        if (!path.startsWith("/api/v1/order-details")) {
            return chain.filter(exchange);
        }

        String key = resolveKey(exchange, authentication);
        Bucket bucket = buckets.computeIfAbsent(key, this::createBucket);

        boolean allowed = bucket.tryConsume(1);

        log.warn("Rate-limit key: {}", key);
        log.warn("ALLOWED: {}", allowed);

        if (allowed) {
            allowedRequests.increment(); // ✅ allowed
            return chain.filter(exchange);
        }

        blockedRequests.increment(); // ❌ blocked

        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = """
                {
                  "error": "Too Many Requests",
                  "message": "Rate limit exceeded. Please try again later.",
                  "key": "%s"
                }
                """.formatted(key.replace("\"", "\\\""));

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes())));
    }

    private String resolveKey(ServerWebExchange exchange, Authentication authentication) {

        if (authentication instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return "user:" + jwt.getSubject();
        }

        var remoteAddress = exchange.getRequest().getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return "ip:" + remoteAddress.getAddress().getHostAddress();
        }

        return "anonymous";
    }
}