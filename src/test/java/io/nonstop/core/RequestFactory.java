package io.nonstop.core;

import io.undertow.server.HttpServerExchange;

public class RequestFactory {

    public static Request create() {
        return create(new HttpServerExchange(null));
    }

    public static Request create(final HttpServerExchange exchange) {
        return new Request(new App(), exchange);
    }

}