package io.nonstop.core;

import io.nonstop.core.util.ConfigMap;
import io.nonstop.core.util.Path;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Object representing the HTTP request.
 *
 * @author John Bailey
 */
public class Response {

    private final App app;

    private final HttpServerExchange exchange;

    private final ConfigMap locals = new ConfigMap();

    Response(App app, HttpServerExchange exchange) {
        this.app = app;
        this.exchange = exchange;
    }

    /**
     * Return the app for this request.
     *
     * @return the current App instance
     */
    public App app() {
        return app;
    }

    /**
     * Return the response local data
     *
     * @return the locals
     */
    public ConfigMap locals() {
        return locals;
    }

    /**
     * Return the header value for the provided name
     *
     * @param headerName the header name
     * @return The value or null if not set
     */
    public String get(final String headerName) {
        return exchange.getResponseHeaders().get(headerName, 0);
    }

    /**
     * Set the value for a header
     *
     * @param headerName the name
     * @param value the value
     * @return the response
     */
    public Response set(final String headerName, final String value) {
        exchange.getResponseHeaders().put(HttpString.tryFromString(headerName), value);
        return this;
    }

    /**
     * Append the value to the header
     *
     * @param headerName the name
     * @param value the value
     * @return the response
     */
    public Response append(final String headerName, final String value) {
        exchange.getResponseHeaders().add(HttpString.tryFromString(headerName), value);
        return this;
    }

    /**
     * Set the response status code
     *
     * @param status the status code
     * @return the response
     */
    public Response status(final int status) {
        exchange.setStatusCode(status);
        return this;
    }

    /**
     * Return the response status code
     *
     * @return the status code
     */
    public int status() {
        return exchange.getStatusCode();
    }

    /**
     * Set the response content type
     *
     * @param type the content type
     * @return the response
     */
    public Response type(final String type) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, type);
        return this;
    }

    /**
     * Get the response content type
     *
     * @return the content type
     */
    public String type() {
        return exchange.getResponseHeaders().get(Headers.CONTENT_TYPE, 0);
    }

    /**
     * Set the response location
     *
     * @param location the location
     * @return the response
     */
    public Response location(final String location) {
        exchange.getResponseHeaders().put(Headers.LOCATION, location);
        return this;
    }

    /**
     * Get the response location
     *
     * @return the location
     */
    public String location() {
        return exchange.getResponseHeaders().get(Headers.LOCATION, 0);
    }

    /**
     * Redirect to a specific location
     *
     * @param location the location
     */
    public void redirect(final String location) {
        this.redirect(302, location);
    }

    /**
     * Redirect to a specific location and status
     *
     * @param status the status
     * @param location the location
     */
    public void redirect(final int status, final String location) {
        status(status).location(location).end();
    }

    /**
     * End the current request response cycle
     */
    public void end() {
        exchange.endExchange();
    }

    /**
     * Send a String response value.  Will automatically end.
     *
     * @param value the string value
     */
    public void send(final String value) {
        exchange.getResponseSender().send(value);
    }

    /**
     * Send a String response value with a specific charset.  Will automatically end.
     *
     * @param value the string value
     * @param charset the character set
     */
    public void send(final String value, final Charset charset) {
        exchange.getResponseSender().send(value, charset);
    }

    /**
     * Send a ByteBuffer response value.  Will automatically end.
     *
     * @param buffer the buffer
     */
    public void send(final ByteBuffer buffer) {
        exchange.getResponseSender().send(buffer);
    }

    /**
     * Send a JSON representation of an Object.  Alias to 'json'
     *
     * @param object the object to jsonify
     */
    public void send(final Object object) {
        json(object);
    }

    /**
     * Send a JSON representation of an Object.  Alias to 'json'
     *
     * @param object the object to jsonify
     */
    public void json(final Object object) {
        // TODO: Serialize JSON
    }

    /**
     * Set the content disposition attachement filename and content type.
     *
     * @param filename the file name for the attachment
     * @return the response
     */
    public Response attachement(final String filename) {
        exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        final String mime = Path.mimetype(filename);
        if ( mime != null ) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, mime);
        }
        return this;
    }


}
