package io.nonstop.core;


import io.nonstop.core.accept.Type;
import io.nonstop.core.accept.WeightedResolver;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Main NonStop application.
 *
 * @author John Bailey
 */
public class App implements io.undertow.server.HttpHandler {

    private Undertow server;

    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        System.out.println("Request:" + exchange);
        System.out.println(WeightedResolver.resolve(exchange.getRequestHeaders().get(Headers.ACCEPT), Type.parser));
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("NO MIDDLEWARE");
    }

    public void listen(final int port) {
        listen(port, "localhost");
    }

    public void listen(final int port, final String host) {
        server = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(this).build();
        server.start();
    }
}
