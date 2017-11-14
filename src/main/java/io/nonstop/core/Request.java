package io.nonstop.core;

import io.nonstop.core.accept.*;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;

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

    private final HttpServerExchange exchange;

    Request(final HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    public Method getMethod() {
        try {
            return Method.valueOf(exchange.getRequestMethod().toString());
        } catch (IllegalArgumentException e) {
            return Method.UNKNOWN;
        }
    }

    public String getProtocol() {
        return exchange.getProtocol().toString();
    }

    public String getHostName() {
        return exchange.getHostName();
    }

    public String getPath() {
        return exchange.getRequestPath();
    }

    public Map<String, Deque<String>> getQuery() {
        // TODO:  Handle nested props:  eg: ?test[foo]=1&test[bar]=2
        final Map<String, Deque<String>> query = exchange.getQueryParameters();
        return query != null ? query : Collections.<String, Deque<String>>emptyMap();
    }

    public String get(final String headerName) {
        return exchange.getRequestHeaders().get(headerName, 0);
    }

    public Map<String, Cookie> getCookies() {
        return exchange.getRequestCookies();
    }

    /**
     * Returns all the content types accepted.
     *
     * @return all accepted types
     */
    public List<String> getAccepts() {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT);
        if (acceptHeader != null) {
            final List<String> values = new LinkedList<>();
            for (Type type : WeightedResolver.resolve(acceptHeader, Type.parser)) {
                values.add(type.toString());
            }
            return values;
        }
        return Collections.emptyList();
    }

    /**
     * Returns the best match from the provided list.
     *
     * @param types the list of desired types
     * @return the best match if one exists
     */
    public String accepts(final String... types) {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT);
        if (acceptHeader != null) {
            for (Type type : WeightedResolver.resolve(acceptHeader, Type.parser)) {
                for (String filter : types) {
                    final String toCheck = filter.contains("/") ? filter : MimeMappings.DEFAULT.getMimeType(filter);
                    if (type.satisfies(toCheck)) {
                        return filter;
                    }
                }

            }
        }
        return types.length > 0 ? types[0] : null;
    }

    /**
     * Returns all the encodings accepted.
     *
     * @return all accepted encodings
     */
    public List<String> getAcceptsEncodings() {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_ENCODING);
        if (acceptHeader != null) {
            final List<String> values = new LinkedList<>();
            for (Encoding encoding : WeightedResolver.resolve(acceptHeader, Encoding.parser)) {
                values.add(encoding.toString());
            }
            return values;
        }
        return Collections.emptyList();
    }

    /**
     * Returns the best encoding match from the provided list.
     *
     * @param encodings the list of desired encodings
     * @return the best match if one exists
     */
    public String acceptsEncodings(final String... encodings) {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_ENCODING);
        if (acceptHeader != null) {
            for (Encoding encoding : WeightedResolver.resolve(acceptHeader, Encoding.parser)) {
                for (String filter : encodings) {
                    if (encoding.satisfies(filter)) {
                        return filter;
                    }
                }
            }
        }
        return encodings.length > 0 ? encodings[0] : null;
    }

    /**
     * Returns all the charsets accepted.
     *
     * @return all accepted charsets
     */
    public List<String> getAcceptsCharsets() {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_CHARSET);
        if (acceptHeader != null) {
            final List<String> values = new LinkedList<>();
            for (Charset charset : WeightedResolver.resolve(acceptHeader, Charset.parser)) {
                values.add(charset.getCharset());
            }
            return values;
        }
        return Collections.emptyList();
    }

    /**
     * Returns the best charset match from the provided list.
     *
     * @param charsets the list of desired charsets
     * @return the best match if one exists
     */
    public String acceptsCharsets(final String... charsets) {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_CHARSET);
        if (acceptHeader != null) {
            for (Charset charset : WeightedResolver.resolve(acceptHeader, Charset.parser)) {
                for (String filter : charsets) {
                    if (charset.satisfies(filter)) {
                        return filter;
                    }
                }
            }
        }
        return charsets.length >  0 ? charsets[0] : null;
    }

    /**
     * Returns all the languages accepted.
     *
     * @return all accepted languages
     */
    public List<String> getAcceptsLanguages() {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_LANGUAGE);
        if (acceptHeader != null) {
            final List<String> values = new LinkedList<>();
            for (Language language : WeightedResolver.resolve(acceptHeader, Language.parser)) {
                values.add(language.getLanguage());
            }
            return values;
        }
        return Collections.emptyList();
    }

    /**
     * Returns the best language match from the provided list.
     *
     * @param languages the list of desired languages
     * @return the best match if one exists
     */
    public String acceptsLanguages(final String... languages) {
        final Deque<String> acceptHeader = exchange.getRequestHeaders().get(Headers.ACCEPT_LANGUAGE);
        if (acceptHeader != null) {
            for (Language language : WeightedResolver.resolve(acceptHeader, Language.parser)) {
                for (String filter : languages) {
                    if (language.satisfies(filter)) {
                        return filter;
                    }
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

}
