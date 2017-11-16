package io.nonstop.core.router;

import io.nonstop.core.Request;
import io.nonstop.core.middleware.Chain;
import io.nonstop.core.middleware.Middleware;
import io.nonstop.core.middleware.Step;

import java.util.Arrays;

public class Router extends Chain {

    public Route route(final Request.Method method, final String path, final Middleware... middleware) {
        return route(method, path, Arrays.asList(middleware));
    }

    public Route route(final Request.Method method, final String path, final Iterable<Middleware> middleware) {
        final Route route = new Route(middleware);
        add(new Step(path, true, route));
        return route;
    }

    public Route get(final String path, final Middleware... middleware) {
        return route(Request.Method.GET, path, middleware);
    }

    public Route get(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.GET, path, middleware);
    }

    public Route post(final String path, final Middleware... middleware) {
        return route(Request.Method.POST, path, middleware);
    }

    public Route post(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.PATCH, path, middleware);
    }

    public Route put(final String path, final Middleware... middleware) {
        return route(Request.Method.PUT, path, middleware);
    }

    public Route put(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.PUT, path, middleware);
    }

    public Route patch(final String path, final Middleware... middleware) {
        return route(Request.Method.PATCH, path, middleware);
    }

    public Route patch(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.PATCH, path, middleware);
    }

    public Route delete(final String path, final Middleware... middleware) {
        return route(Request.Method.DELETE, path, middleware);
    }

    public Route delete(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.DELETE, path, middleware);
    }

    public Route options(final String path, final Middleware... middleware) {
        return route(Request.Method.OPTIONS, path, middleware);
    }

    public Route options(final String path, final Iterable<Middleware> middleware) {
        return route(Request.Method.OPTIONS, path, middleware);
    }
}
