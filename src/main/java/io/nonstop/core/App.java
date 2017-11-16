package io.nonstop.core;


import io.nonstop.core.middleware.Chain;
import io.nonstop.core.middleware.Context;
import io.nonstop.core.router.Router;
import io.nonstop.core.util.ConfigMap;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;



/**
 * Main NonStop application.
 *
 * @author John Bailey
 */
public class App extends Router {

    private Undertow server;

    private final ConfigMap locals = new ConfigMap();

    private void handleRequest(final HttpServerExchange exchange) throws Exception {
        final Request request = new Request(this, exchange);
        final Response response = new Response(this, exchange);
        handle(request, response, new Context() {
            @Override
            public void proceed() {
                exchange.setStatusCode(404).getResponseSender().send("Not Found");
            }

            @Override
            public void fail(final Throwable t) {
                fail(t.getMessage());
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
