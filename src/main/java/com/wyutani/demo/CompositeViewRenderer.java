package com.wyutani.demo;

import java.nio.charset.StandardCharsets;

import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.reactive.result.method.InvocableHandlerMethod;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CompositeViewRenderer implements HandlerResultHandler {
    
    private final ViewResolver resolver;

    public CompositeViewRenderer(ViewResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean supports(HandlerResult result) {
        if (Publisher.class.isAssignableFrom(result.getReturnType().toClass())) {
            if (Rendering.class.isAssignableFrom(result.getReturnType().getGeneric(0).toClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Mono<void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        String[] methodAnnotation = ((InvocableHandlerMethod) result.getHandler())
                .getMethodAnnotation(RequestMapping.class).produces();
        MediaType type = methodAnnotation.length > 0 ? MediaType.valueOf(methodAnnotation[0]) : MediaType.TEXT_HTML;
        exchange.getResponse().getHeaders().setContentType(type);
        boolean sse = MediaType.TEXT_EVENT_STREAM.includes(type);
        @SuppressWarnings("unchecked")
        Flux<Rendering> renderings = Flux.from((Publisher<Rendering>) result.getReturnValue());
        final ExchangeWraper wrapper = new ExchangeWrapper(exchange);
        // TODO: Continue CompositeViewRenderer.java -> /home/dacevedom/spring-boot-js-demo/htmx
    }
}
