package com.srishti.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Map;

@Component
public class AuthenticationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final RouteValidator validator;
    private final RestTemplate restTemplate;

    @Value("${urls.validate}")
    private String validateUrl;

    public AuthenticationGatewayFilterFactory(RouteValidator validator, RestTemplate restTemplate) {
        super(Config.class);
        this.validator = validator;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                ResponseEntity<Map<Long, String>> responseEntity = sendValidationRequest(exchange);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    ServerHttpRequest request = addUserHeaders(responseEntity.getBody(), exchange);
                    return chain.filter(
                            exchange.mutate()
                                    .request(request)
                                    .build()
                    );
                } else {
                    throw new RuntimeException("Request failed with status code: " + responseEntity.getStatusCode());
                }
            }
            return chain.filter(
                    exchange.mutate()
                            .build()
            );
        };
    }

    public static class Config {
    }

    private ResponseEntity<Map<Long, String>> sendValidationRequest(ServerWebExchange exchange) {
        HttpHeaders headers = new HttpHeaders();
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        headers.set("Authorization", jwt);
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(validateUrl));
        return restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Map<Long, String>>() {}); // TODO: WebFlux exception handling
    }

    private ServerHttpRequest addUserHeaders(Map<Long, String> body, ServerWebExchange exchange) {
        Map.Entry<Long, String> entry = body.entrySet().iterator().next();
        Long userId = entry.getKey();
        String role = entry.getValue();
        System.out.println("Adding owner: " + userId + " with role: " + role);
        return exchange.getRequest()
                .mutate()
                .header("ownerId", String.valueOf(userId))
                .header("role", String.valueOf(role))
                .build();
    }
}
