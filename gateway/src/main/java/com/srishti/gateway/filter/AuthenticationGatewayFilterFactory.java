package com.srishti.gateway.filter;

import com.srishti.gateway.exception.UserInvalidAuthenticationException;
import com.srishti.gateway.exception.UserNotAuthorizedException;
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

    @Value("${urls.checkExpiry}")
    private String checkExpiryUrl;

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
                    Map<Long, String> responseBody = responseEntity.getBody();

                    if (responseBody != null && !responseBody.isEmpty()) {
                        Map.Entry<Long, String> entry = responseBody.entrySet().iterator().next();
                        Long userId = entry.getKey();
                        String role = entry.getValue();

                        // Perform role-based access control
                        // TODO: use appropriate exceptions
                        if (!role.equals("OWNER") && exchange.getRequest().getURI().getPath().startsWith("/api/v1")) {
                            throw new UserNotAuthorizedException("You are not an Owner");
                        }
                        if (role.equals("OWNER") && isUserAccountExpired(userId)) {
                            throw new UserNotAuthorizedException("Your Account Subscription is expired. " +
                                    "Please contact to billing team");
                        }
                        ServerHttpRequest request = addUserHeaders(userId, exchange);
                        return chain.filter(
                                exchange.mutate()
                                        .request(request)
                                        .build()
                        );
                    }
                } else {
                    throw new UserInvalidAuthenticationException("Invalid credentials");
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

    private ServerHttpRequest addUserHeaders(Long userId, ServerWebExchange exchange) {
        System.out.println("Adding owner: " + userId);
        return exchange.getRequest()
                .mutate()
                .header("ownerId", String.valueOf(userId))
                .build();
    }

    private boolean isUserAccountExpired(Long userId) {
        System.out.println("started");
        HttpHeaders headers = new HttpHeaders();
        headers.set("ownerId", userId.toString());
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers,
                HttpMethod.GET,
                URI.create(checkExpiryUrl));
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<Boolean>() {});
        System.out.println(responseEntity);
        return responseEntity.getBody()!=null && responseEntity.getBody();
    }

    public static class AccountExpiredException extends RuntimeException {
        public AccountExpiredException(String message) {
            super(message);
        }
    }
}
