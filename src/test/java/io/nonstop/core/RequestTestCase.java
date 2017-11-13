package io.nonstop.core;

import io.undertow.server.AbstractServerConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.Protocols;
import org.junit.Before;
import org.junit.Test;
import org.xnio.OptionMap;


import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Test Request object.
 *
 * @author John Bailey
 */
public class RequestTestCase {

    private HttpServerExchange exchange;
    private Request request;
    private ServerConnection connection;

    @Before
    public void setUp() {
        connection = createMock(AbstractServerConnection.class);
        exchange = new HttpServerExchange(connection);
        request = new Request(exchange);
    }

    @Test
    public void testMethodKnown() {
        exchange.setRequestMethod(Methods.GET);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testMethodUnknown() {
        exchange.setRequestMethod(Methods.LOCK);
        assertEquals(Request.Method.UNKNOWN, request.getMethod());
    }

    @Test
    public void testProtocol() {
        final HttpString protocol = Protocols.HTTP_1_1;
        exchange.setProtocol(protocol);
        assertEquals(protocol.toString(), request.getProtocol());
    }

    @Test
    public void testHostName() {
        final String hostName = "localhost";
        exchange.getRequestHeaders().put(Headers.HOST, hostName);
        assertEquals(hostName, request.getHostName());
    }

    @Test
    public void testPath() {
        final String path = "/some/path/to/something";
        exchange.setRequestPath(path);
        assertEquals(path, request.getPath());
    }

    @Test
    public void testQuery() {
        exchange.addQueryParam("test1", "value");
        exchange.addQueryParam("test2", "value2");
        assertEquals("value", request.getQuery().get("test1").getFirst());
        assertEquals("value2", request.getQuery().get("test2").getFirst());
        assertNull(request.getQuery().get("not-found"));
    }

    @Test
    public void testGet() {
        exchange.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getRequestHeaders().put(Headers.AUTHORIZATION, "Bearer 12345");
        assertEquals("text/plain", request.get("Content-Type"));
        assertEquals("Bearer 12345", request.get("Authorization"));
        assertNull(request.get("not-found"));
    }

    @Test
    public void testCookies() {
        expect(connection.getUndertowOptions()).andReturn(OptionMap.EMPTY).anyTimes();
        replay(connection);
        exchange.getRequestCookies().put("session", new CookieImpl("session", "12345abcde"));
        assertEquals("12345abcde", request.getCookies().get("session").getValue());
    }

    @Test
    public void testAccepts() {
        exchange.getRequestHeaders().put(Headers.ACCEPT, "application/json,text/html");
        final List<String> accepts = request.accepts();
        assertNotNull(accepts);
        assertEquals(2, accepts.size());
        assertEquals("application/json", accepts.get(0));
        assertEquals("text/html", accepts.get(1));
    }

    @Test
    public void testAcceptsWithFilter() {
        exchange.getRequestHeaders().put(Headers.ACCEPT, "application/json,text/html");
        final String accepts = request.accepts("application/json", "text/html");
        assertNotNull(accepts);
        assertEquals("application/json", accepts);
    }

    @Test
    public void testAcceptsWithFilterWeighted() {
        exchange.getRequestHeaders().put(Headers.ACCEPT, "application/json;q=0.8,text/html");
        final String accepts = request.accepts("application/json", "text/html");
        assertNotNull(accepts);
        assertEquals("text/html", accepts);
    }

    @Test
    public void testAcceptsEncodings() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_ENCODING, "gzip,compress");
        final List<String> accepts = request.acceptsEncodings();
        assertNotNull(accepts);
        assertEquals(2, accepts.size());
        assertEquals("gzip", accepts.get(0));
        assertEquals("compress", accepts.get(1));
    }

    @Test
    public void testAcceptsEncodingsWithFilter() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_ENCODING, "gzip,compress");
        final String accepts = request.acceptsEncodings("gzip", "compress");
        assertNotNull(accepts);
        assertEquals("gzip", accepts);
    }

    @Test
    public void testAcceptsEncodingsWithFilterWeighted() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_ENCODING, "gzip;q=0.8,compress");
        final String accepts = request.acceptsEncodings("gzip", "compress");
        assertNotNull(accepts);
        assertEquals("compress", accepts);
    }

    @Test
    public void testAcceptsCharsets() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_CHARSET, "utf-8,utf-16");
        final List<String> accepts = request.acceptsCharsets();
        assertNotNull(accepts);
        assertEquals(2, accepts.size());
        assertEquals("utf-8", accepts.get(0));
        assertEquals("utf-16", accepts.get(1));
    }

    @Test
    public void testAcceptsCharsetsWithFilter() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_CHARSET, "utf-8,utf-16");
        final String accepts = request.acceptsCharsets("utf-8", "utf-16");
        assertNotNull(accepts);
        assertEquals("utf-8", accepts);
    }

    @Test
    public void testAcceptsCharsetsWithFilterWeighted() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_CHARSET, "utf-8;q=0.8,utf-16");
        final String accepts = request.acceptsCharsets("utf-8", "utf-16");
        assertNotNull(accepts);
        assertEquals("utf-16", accepts);
    }

    @Test
    public void testAcceptsLanguages() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_LANGUAGE, "en,fr");
        final List<String> accepts = request.acceptsLanguages();
        assertNotNull(accepts);
        assertEquals(2, accepts.size());
        assertEquals("en", accepts.get(0));
        assertEquals("fr", accepts.get(1));
    }

    @Test
    public void testAcceptsLanguagesWithFilter() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_LANGUAGE, "en,fr");
        final String accepts = request.acceptsLanguages("en" , "fr");
        assertNotNull(accepts);
        assertEquals("en", accepts);
    }

    @Test
    public void testAcceptsLanguagesWithFilterWeighted() {
        exchange.getRequestHeaders().put(Headers.ACCEPT_LANGUAGE, "en;q=0.8,fr");
        final String accepts = request.acceptsLanguages("en" , "fr");
        assertNotNull(accepts);
        assertEquals("fr", accepts);
    }
}
