package io.nonstop.core;

import io.nonstop.core.accept.*;
import io.nonstop.core.util.data.DataNode;
import io.nonstop.core.util.data.ValueDataNode;
import io.undertow.io.UndertowInputStream;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;

import java.io.InputStream;
import java.util.*;

/**
 * Object representing the HTTP request.
 *
 * @author John Bailey
 */
public class Request {

    public enum Method {
        OPTIONS, HEAD, GET, POST, PUT, PATCH, DELETE, UNKNOWN;
    }

    private final App app;

    private final HttpServerExchange exchange;

    private String path;

    private Map<String, String> params = new HashMap<>();

    private List<Type> resolvedAccepts;

    private List<Encoding> resolvedAcceptsEncodings;

    private List<Charset> resolvedAcceptsCharsets;

    private List<Language> resolvedAcceptsLanguages;

    private DataNode body = new ValueDataNode(null);

    Request(final App app, final HttpServerExchange exchange) {
        this.app = app;
        this.exchange = exchange;
        this.path = exchange.getRequestPath();
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
     * Return the request body.
     *
     * @return the body
     */
    public DataNode body() {
        return body;
    }

    public void body(final DataNode body) {
        this.body = body;
    }

    /**
     * Get the request method
     *
     * @return the method
     */
    public Method method() {
        try {
            return Method.valueOf(exchange.getRequestMethod().toString());
        } catch (IllegalArgumentException e) {
            return Method.UNKNOWN;
        }
    }

    /**
     * Get the request protocol
     *
     * @return the protocol
     */
    public String protocol() {
        return exchange.getProtocol().toString();
    }

    /**
     * Get the request host name
     *
     * @return the host name
     */
    public String hostname() {
        return exchange.getHostName();
    }

    /**
     * Get the request path
     *
     * @return the path
     */
    public String path() {
        return path;
    }

    /**
     * Set the current request path.
     *
     * @param path the current path
     */
    public void path(final String path) {
        this.path = path;
    }

    /**
     * Get the query parameters
     *
     * @return the parameters
     */
    public Map<String, Deque<String>> query() {
        // TODO:  Handle nested props:  eg: ?test[foo]=1&test[bar]=2
        final Map<String, Deque<String>> query = exchange.getQueryParameters();
        return query != null ? query : Collections.<String, Deque<String>>emptyMap();
    }

    /**
     * Get the request path parameters.
     *
     * @return the params
     */
    public Map<String, String> params() {
        return params;
    }

    /**
     * Get the value for a given header name.
     *
     * @param headerName the header name
     * @return returns the header value
     */
    public String get(final String headerName) {
        return exchange.getRequestHeaders().get(headerName, 0);
    }

    /**
     * Get the request cookies.
     *
     * @return the cookies
     */
    public Map<String, Cookie> cookies() {
        return exchange.getRequestCookies();
    }

    private List<Type> resolvedAccepts() {
        if (resolvedAccepts != null) {
            return resolvedAccepts;
        }
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT);
        this.resolvedAccepts = acceptHeader != null ? WeightedResolver.resolve(acceptHeader, Type.parser) : Collections.<Type>emptyList();
        return resolvedAccepts;
    }

    /**
     * Returns all the content types accepted.
     *
     * @return all accepted types
     */
    public List<String> accepts() {
        final List<String> values = new LinkedList<>();
        for (Type type : resolvedAccepts()) {
            values.add(type.toString());
        }
        return values;
    }

    /**
     * Returns the best match from the provided list.
     *
     * @param types the list of desired types
     * @return the best match if one exists
     */
    public String accepts(final String... types) {
        for (Type type : resolvedAccepts()) {
            for (String filter : types) {
                final String toCheck = filter.contains("/") ? filter : MimeMappings.DEFAULT.getMimeType(filter);
                if (type.satisfies(toCheck)) {
                    return filter;
                }
            }

        }
        return types.length > 0 ? types[0] : null;
    }

    private List<Encoding> resolvedAcceptsEncodings() {
        if (resolvedAcceptsEncodings != null) {
            return resolvedAcceptsEncodings;
        }
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_ENCODING);
        this.resolvedAcceptsEncodings = acceptHeader != null ? WeightedResolver.resolve(acceptHeader, Encoding.parser) : Collections.<Encoding>emptyList();
        return resolvedAcceptsEncodings;
    }

    /**
     * Returns all the encodings accepted.
     *
     * @return all accepted encodings
     */
    public List<String> acceptsEncodings() {
        final List<String> values = new LinkedList<>();
        for (Encoding encoding : resolvedAcceptsEncodings()) {
            values.add(encoding.toString());
        }
        return values;
    }

    /**
     * Returns the best encoding match from the provided list.
     *
     * @param encodings the list of desired encodings
     * @return the best match if one exists
     */
    public String acceptsEncodings(final String... encodings) {
        for (Encoding encoding : resolvedAcceptsEncodings()) {
            for (String filter : encodings) {
                if (encoding.satisfies(filter)) {
                    return filter;
                }
            }
        }
        return encodings.length > 0 ? encodings[0] : null;
    }

    private List<Charset> resolveAcceptsCharsets() {
        if (resolvedAcceptsCharsets != null) {
            return resolvedAcceptsCharsets;
        }
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_CHARSET);
        this.resolvedAcceptsCharsets = acceptHeader != null ? WeightedResolver.resolve(acceptHeader, Charset.parser) : Collections.<Charset>emptyList();
        return resolvedAcceptsCharsets;
    }

    /**
     * Returns all the charsets accepted.
     *
     * @return all accepted charsets
     */
    public List<String> acceptsCharsets() {
        final List<String> values = new LinkedList<>();
        for (Charset charset : resolveAcceptsCharsets()) {
            values.add(charset.getCharset());
        }
        return values;
    }

    /**
     * Returns the best charset match from the provided list.
     *
     * @param charsets the list of desired charsets
     * @return the best match if one exists
     */
    public String acceptsCharsets(final String... charsets) {
        for (Charset charset : resolveAcceptsCharsets()) {
            for (String filter : charsets) {
                if (charset.satisfies(filter)) {
                    return filter;
                }
            }
        }
        return charsets.length >  0 ? charsets[0] : null;
    }

    private List<Language> resolvedAcceptsLanguages() {
        if (resolvedAcceptsLanguages != null) {
            return resolvedAcceptsLanguages;
        }
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_LANGUAGE);
        this.resolvedAcceptsLanguages = acceptHeader != null ? WeightedResolver.resolve(acceptHeader, Language.parser) : Collections.<Language>emptyList();
        return resolvedAcceptsLanguages;
    }

    /**
     * Returns all the languages accepted.
     *
     * @return all accepted languages
     */
    public List<String> acceptsLanguages() {
        final List<String> values = new LinkedList<>();
        for (Language language : resolvedAcceptsLanguages()) {
            values.add(language.getLanguage());
        }
        return values;
    }

    /**
     * Returns the best language match from the provided list.
     *
     * @param languages the list of desired languages
     * @return the best match if one exists
     */
    public String acceptsLanguages(final String... languages) {
        for (Language language : resolvedAcceptsLanguages()) {
            for (String filter : languages) {
                if (language.satisfies(filter)) {
                    return filter;
                }
            }
        }
        return languages.length > 0 ? languages[0] : null;
    }

    /**
     * Determine whether the request content type is satisfied by the provided type.
     *
     * @param typeStr the content type to check
     * @return true if satisfied, false if not
     */
    public boolean is(final String typeStr) {
        final Deque<String> typeHeader = exchange.getRequestHeaders().get(Headers.CONTENT_TYPE);
        if (typeHeader != null) {
            final String toCheck = typeStr.contains("/") ? typeStr : MimeMappings.DEFAULT.getMimeType(typeStr);
            final Type type = Type.parser.parse(toCheck);
            return type.satisfies(typeHeader.peek());
        }
        return false;
    }

    /**
     * Return the input stream for the request.
     *
     * @return the request stream
     */
    public InputStream stream() {
        return new UndertowInputStream(exchange);
    }

    public void dispatch(final Runnable runnable) {
        exchange.dispatch(null, runnable);
    }

    @Override
    public String toString() {
        return "Request[" + method().toString() + " " + path() + " " + protocol() +  "]";
    }
}
