package io.nonstop.core.middleware;

import io.nonstop.core.Request;
import io.nonstop.core.Response;

public interface Middleware {

    void handle(Request req, Response res, Next next);

}
