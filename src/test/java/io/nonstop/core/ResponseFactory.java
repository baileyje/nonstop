package io.nonstop.core;

import io.undertow.server.HttpServerExchange;

public class ResponseFactory {

    public static Response create() {
        return create(new HttpServerExchange(null));
    }

    public static Response create(final HttpServerExchange exchange) {
        return new Response(new App(), exchange);
    }
}
