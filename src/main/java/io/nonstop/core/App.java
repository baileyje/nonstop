package io.nonstop.core;


import io.nonstop.core.middleware.Middleware;
import io.nonstop.core.middleware.Next;
import io.nonstop.core.util.ConfigMap;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.ByteBuffer;

/**
 * Main NonStop application.
 *
 * @author John Bailey
 */
public class App {

    private Undertow server;

    private final ConfigMap locals = new ConfigMap();

    private Middleware middleware;

    private void handleRequest(final HttpServerExchange exchange) throws Exception {
        if (middleware == null) {
            exchange.setStatusCode(404).getResponseSender().send("Not Found");
            return;
        }
        final Request request = new Request(this, exchange);
        final Response response = new Response(this, exchange);
        middleware.handle(request, response, new Next() {
            @Override
            public void proceed() {
                exchange.setStatusCode(404).getResponseSender().send("Not Found");
            }

            @Override
            public void fail(final Exception e) {
                fail(e.getMessage());
            }

            @Override
            public void fail(final String message) {
                exchange.setStatusCode(500).getResponseSender().send(message);
            }
        });
    }

    /**
     * Start listening for requests on the provided port.
     *
     * @param port the listen port
     */
    public void listen(final int port) {
        listen(port, "localhost");
    }

    /**
     * * Start listening for requests on the provided port and host.
     *
     * @param port the listen port
     * @param host the listen host
     */
    public void listen(final int port, final String host) {
        server = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(new Listener(this)).build();
        server.start();
    }

    /**
     * Stop listening for requests.
     */
    public void stop() {
        server.stop();
    }

    /**
     * Return the app local data
     *
     * @return the locals
     */
    public ConfigMap locals() {
        return locals;
    }

    public void use(final Middleware middleware) {
        this.middleware = middleware;
    }

    private class Listener  implements HttpHandler {

        private App app;

        Listener(App app) {
            this.app = app;
        }

        public void handleRequest(final HttpServerExchange exchange) throws Exception {
            app.handleRequest(exchange);
        }

    }
}
