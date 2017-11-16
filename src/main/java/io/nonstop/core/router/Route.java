package io.nonstop.core.router;

import io.nonstop.core.middleware.Chain;
import io.nonstop.core.middleware.Middleware;

public class Route extends Chain {

    Route(final Iterable<Middleware> middleware) {
        use(middleware);
    }
}
