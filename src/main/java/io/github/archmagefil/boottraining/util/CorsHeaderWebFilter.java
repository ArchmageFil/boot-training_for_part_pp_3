package io.github.archmagefil.boottraining.util;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Нужен для нормальной разработки в условиях запущенного сервера и загруженной
 * с диска страницы html.
 */
@Component
public class CorsHeaderWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().setAccessControlAllowOrigin("*");
        return chain.filter(exchange);
    }
}
