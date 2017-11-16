package io.nonstop.core.middleware;

import io.nonstop.core.Request;
import io.nonstop.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a chain of middleware in invocation order.
 *
 * @author John Bailey
 */
public class Chain implements Middleware {

    static final String DEFAULT_PATH = "/";

    private final List<Step> steps = new ArrayList<>();

    @Override
    public void handle(final Request req, final Response res, final Context ctx) {
        new Invocation(steps, req, res, ctx).proceed();
    }

    /**
     * Use the provided middleware for requests matching the provided path.
     *
     * @param path the path to match
     * @param middleware the middleware to execute
     * @return the chain
     */
    public Chain use(final String path, final Middleware... middleware) {
        this.use(path, Arrays.asList(middleware));
        return this;
    }

    /**
     * Use the provided middleware for requests matching the provided path.
     *
     * @param path the path to match
     * @param middleware the middleware to execute
     * @return the chain
     */
    public Chain use(final String path, final Iterable<Middleware> middleware) {
        for (Middleware item : middleware) {
            add(new Step(path, false, item));
        }
        return this;
    }

    /**
     * Use the provided middleware for all requests.
     *
     * @param middleware the middleware
     * @return the chain
     */
    public Chain use(final Middleware... middleware) {
        this.use(DEFAULT_PATH, middleware);
        return this;
    }

    /**
     * Use the provided middleware for all requests.
     *
     * @param middleware the middleware
     * @return the chain
     */
    public Chain use(final Iterable<Middleware> middleware) {
        this.use(DEFAULT_PATH, middleware);
        return this;
    }

    protected void add(final Step step) {
        steps.add(step);
    }

    class Invocation implements Context {

        private final Iterator<Step> steps;

        private final Context delegate;

        private final Request req;

        private final Response res;

        private String lastPath;

        public Invocation(final Iterable<Step> steps, final Request req, final Response res, final Context delegate) {
            this.steps = steps.iterator();
            this.delegate = delegate;
            this.req = req;
            this.res = res;
        }

        @Override
        public void proceed() {
            if (lastPath != null) {
                req.path(lastPath);
                lastPath = null;
            }
            if (steps.hasNext()) {
                final Step context = steps.next();
                final Step.MatchResult result = context.matches(req.path());
                if (result !=  null) {
                    lastPath = req.path();
                    if (!context.end) {
                        req.path(result.remainingPath);
                    }
                    req.params().putAll(result.params);
                    try {
                        context.middleware.handle(req, res, this);
                    } catch (Throwable t) {
                        delegate.fail(t);
                    }
                } else {
                    proceed();
                }
            } else {
                delegate.proceed();
            }
        }

        @Override
        public void fail(final Throwable t) {
            delegate.fail(t);
        }

        @Override
        public void fail(final String message) {
            delegate.fail(message);
        }
    }

}
