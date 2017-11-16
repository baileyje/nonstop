package io.nonstop.core.middleware;

import io.nonstop.core.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test chain.
 *
 * @author John Bailey
 */
public class ChainTestCase {

    private final Request request = RequestFactory.create();

    private final Response response = ResponseFactory.create();

    private final MockContext mockContext = new MockContext();

    final Chain chain = new Chain();

    @Test
    public void testHandleEmptyChain() {
        chain.handle(request, response, mockContext);
        assertTrue(mockContext.proceedCalled);
    }

    @Test
    public void testHandlesPathlessMW() {
        final AtomicBoolean called = new AtomicBoolean();
        chain.use((req, res, ctx) -> {
            called.set(true);
            ctx.proceed();
        });
        chain.handle(request, response, mockContext);
        assertTrue(called.get());
    }

    @Test
    public void testHandlesNonMatchingPathMW() {
        final AtomicBoolean called = new AtomicBoolean();
        chain.use("/foo", (req, res, ctx) -> {
            called.set(true);
            ctx.proceed();
        });
        request.path("/bar");
        chain.handle(request, response, mockContext);
        assertFalse(called.get());
    }

    @Test
    public void testHandlesMatchingPathMW() {
        final AtomicBoolean called = new AtomicBoolean();
        chain.use("/foo", (req, res, ctx) -> {
            called.set(true);
            ctx.proceed();
        });
        request.path("/foo");
        chain.handle(request, response, mockContext);
        assertTrue(called.get());
    }

    @Test
    public void testMaintainsPath() {
        final AtomicBoolean called = new AtomicBoolean();
        chain.use("/foo", (req, res, ctx) -> {
            assertEquals("/bar", req.path());
            called.set(true);
            ctx.proceed();
        });
        request.path("/foo/bar");
        chain.handle(request, response, mockContext);
        assertTrue(called.get());
        assertEquals("/foo/bar", request.path());
    }

    @Test
    public void testDelegatesFail() {
        final Exception expected = new Exception("Failed");
        chain.use((req, res, context) -> {
            context.fail(expected);
        });
        chain.handle(request, response, mockContext);
        assertEquals(expected, mockContext.failError);
    }

    @Test
    public void testDelegatesFailMessage() {
        final String expected = "Failed";
        chain.use((req, res, context) -> {
            context.fail(expected);
        });

        chain.handle(request, response, mockContext);
        assertEquals(expected, mockContext.failMessage);
    }

    @Test
    public void testDelegatesProceed() {
        chain.use((req, res, context) -> {
           context.proceed();
        });

        chain.handle(request, response, mockContext);
        assertTrue(mockContext.proceedCalled);
    }

    @Test
    public void testRequestParams() {
        final AtomicBoolean called = new AtomicBoolean();
        chain.use("/foo/:param1/bar/:param2", (req, res, ctx) -> {
            called.set(true);
            assertEquals(new HashMap<String, String>() {{
                put("param1", "value1");
                put("param2", "value2");
            }}, req.params());
            ctx.proceed();
        });
        request.path("/foo/value1/bar/value2");
        chain.handle(request, response, mockContext);
        assertTrue(called.get());
    }

    @Test
    public void testHandleRuntimeException() {
        final RuntimeException expected = new RuntimeException("Failed");
        chain.use((req, res, context) -> {
            throw expected;
        });
        chain.handle(request, response, mockContext);
        assertEquals(expected, mockContext.failError);
    }

    private class MockContext implements Context {

        private boolean proceedCalled;

        private Throwable failError;

        private String failMessage;

        @Override
        public void proceed() {
            proceedCalled = true;
        }

        @Override
        public void fail(final Throwable t) {
            failError = t;
        }

        @Override
        public void fail(final String message) {
            failMessage = message;
        }
    }
}
