package io.nonstop.core.middleware;

import io.nonstop.core.Request;
import io.nonstop.core.Response;

/**
 * A middleware component.  This represents the base of the framework.
 * All web requests are processed by one or more middleware components.
 *
 * @author John Bailey
 */
public interface Middleware {

    /**
     * Handle a web request.  Use date from request and manipulate the response.
     * Either use the response to terminate the request (send, render, etc), or use
     * the context to continue processing.
     *
     * @param req the request
     * @param res the response
     * @param context the context
     */
    void handle(Request req, Response res, Context context);

}
