package org.example.config;

import io.github.bucket4j.Bucket;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createBucket(String s) {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(10) // burst (максимум одразу)
                        .refillGreedy(5, Duration.ofSeconds(1)) // 5 токенів/сек
                )
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> applyRateLimit(exchange, chain, authentication))
                .switchIfEmpty(applyRateLimit(exchange, chain, null));
    }

    private Mono<Void> applyRateLimit(ServerWebExchange exchange,
                                      GatewayFilterChain chain,
                                      Authentication authentication) {

        String key = resolveKey(exchange, authentication);
        System.out.println(key);
        Bucket bucket = buckets.computeIfAbsent(key, this::createBucket);

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        }

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

    private String resolveKey(ServerWebExchange exchange, Object authentication) {
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

    @Override
    public int getOrder() {
        return -10;
    }
}