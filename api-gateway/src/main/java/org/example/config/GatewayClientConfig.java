package org.example.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) ->
                        ReactiveSecurityContextHolder.getContext()
                                .map(SecurityContext::getAuthentication)
                                .flatMap(auth -> {
                                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                                        Jwt jwt = jwtAuth.getToken();
                                        ClientRequest newRequest = ClientRequest.from(request)
                                                .header("X-User-Id", jwt.getSubject())
                                                .header("X-Roles",
                                                        String.join(",", jwt.getClaimAsStringList("roles")))
                                                .build();

                                        return next.exchange(newRequest);
                                    }

                                    return next.exchange(request);
                                })
                )
                .build();

    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange ->
                ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .map(auth -> {
                            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                                return jwtAuth.getToken().getSubject();
                            }
                            return "anonymous";
                        })
                        .defaultIfEmpty("anonymous");
    }
}
