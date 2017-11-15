package io.nonstop.core;

import io.undertow.server.AbstractServerConnection;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ResponseTestCase {

    private HttpServerExchange exchange;
    private Response response;
    private ServerConnection connection;

    @Before
    public void setUp() {
        connection = createNiceMock(AbstractServerConnection.class);
        exchange = new HttpServerExchange(connection);
        response = new Response(new App(), exchange);
    }

    @Test
    public void testGet() {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, "10");
        assertEquals("text/plain", response.get("Content-Type"));
        assertEquals("10", response.get("Content-Length"));
        assertNull(response.get("not-found"));
    }

    @Test
    public void testSet() {
        response.set("Content-Type", "text/plain");
        response.set("Content-Length", "10");
        assertEquals("text/plain", exchange.getResponseHeaders().get(Headers.CONTENT_TYPE).getFirst());
        assertEquals("10", exchange.getResponseHeaders().get(Headers.CONTENT_LENGTH).getFirst());
    }

    @Test
    public void testAppend() {
        response.append("Cookie", "test1=value1");
        response.append("Cookie", "test2=value2");
        response.append("Content-Length", "10");
        assertEquals("test1=value1", exchange.getResponseHeaders().get(Headers.COOKIE).getFirst());
        assertEquals("test2=value2", exchange.getResponseHeaders().get(Headers.COOKIE).get(1));
        assertEquals("10", exchange.getResponseHeaders().get(Headers.CONTENT_LENGTH).getFirst());
    }

    @Test
    public void testStatus() {
        assertEquals(200, exchange.getStatusCode());
        response.status(201);
        assertEquals(201, exchange.getStatusCode());
        assertEquals(201, response.status());
    }

    @Test
    public void testType() {
        assertNull(exchange.getResponseHeaders().get(Headers.CONTENT_TYPE));
        response.type("text/html");
        assertEquals("text/html", exchange.getResponseHeaders().get(Headers.CONTENT_TYPE).getFirst());
        assertEquals("text/html", response.type());
    }

    @Test
    public void testLocation() {
        assertNull(exchange.getResponseHeaders().get(Headers.LOCATION));
        response.location("/some/path");
        assertEquals("/some/path", exchange.getResponseHeaders().get(Headers.LOCATION).getFirst());
        assertEquals("/some/path", response.location());
    }

    private void readyExhangeForEnd() throws Exception {
        exchange.setRequestMethod(Methods.GET);
        expect(connection.isOpen()).andReturn(false).anyTimes();
        replay(connection);
        Class<HttpServerExchange> clazz = HttpServerExchange.class;
        Method meth = clazz.getDeclaredMethod("terminateRequest");
        meth.setAccessible(true);
        meth.invoke(exchange);
    }


    @Test
    public void testRedirect() throws Exception {
        readyExhangeForEnd();
        response.redirect("/some/path");
        assertEquals("/some/path", exchange.getResponseHeaders().get(Headers.LOCATION).getFirst());
        assertEquals("/some/path", response.location());
        assertEquals(302, response.status());
    }

    @Test
    public void testRedirectWithStatus() throws Exception {
        readyExhangeForEnd();
        response.redirect(301, "/some/path");
        assertEquals("/some/path", exchange.getResponseHeaders().get(Headers.LOCATION).getFirst());
        assertEquals("/some/path", response.location());
        assertEquals(301, response.status());
    }

    @Test
    public void testAttachment() {
        response.attachement("test.png");
        assertEquals("attachment; filename=\"test.png\"", exchange.getResponseHeaders().get(Headers.CONTENT_DISPOSITION).getFirst());
        assertEquals("image/png", exchange.getResponseHeaders().get(Headers.CONTENT_TYPE).getFirst());
    }
}
