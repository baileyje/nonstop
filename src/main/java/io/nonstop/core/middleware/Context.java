package io.nonstop.core.middleware;

/**
 * Context for manipulating the middleware chain processing.
 *
 * @author John Bailey
 */
public interface Context {

    /**
     * Continue with the middleware processing.
     */
    void proceed();

    /**
     * Fail the request with the provided error.
     *
     * @param t the error
     */
    void fail(final Throwable t);

    /**
     * Fail the request with the provided error message.
     *
     * @param message the error message
     */
    void fail(final String message);

}
