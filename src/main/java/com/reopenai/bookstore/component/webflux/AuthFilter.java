package com.reopenai.bookstore.component.webflux;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Mock User Authentication
 * <p>
 * Created by Allen Huang
 */
@Component
public class AuthFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getAttributes().put(AttrKeys.CURRENT_UID, 1L);
        return chain.filter(exchange);
    }

}
