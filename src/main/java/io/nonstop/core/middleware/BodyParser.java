package io.nonstop.core.middleware;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nonstop.core.Request;
import io.nonstop.core.Response;
import io.nonstop.core.util.data.ValueDataNode;
import io.undertow.server.handlers.form.FormParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.xnio.IoUtils.safeClose;

public class BodyParser {

    static final ObjectMapper MAPPER = new ObjectMapper();

    public static Middleware json = (req, res, ctx) -> {
        final InputStream stream = req.stream();
        req.dispatch(() -> {
            try {
                final JsonNode node = MAPPER.readTree(stream);
                Object value = null;
                if (node.isObject() ) {
                    value = MAPPER.convertValue(node, Map.class);
                } else if (node.isArray()) {
                    value = MAPPER.convertValue(node, List.class);
                }
                req.body(new ValueDataNode(value));
                ctx.proceed();
            } catch (IOException e) {
                ctx.fail(e);
            } finally {
                safeClose(stream);
            }
        });
    };

    public static Middleware urlEncoded = new Middleware() {
        @Override
        public void handle(Request req, Response res, Context ctx) {
            // TODO:
            ctx.proceed();
        }
    };

    public static Middleware text = new Middleware() {
        @Override
        public void handle(Request req, Response res, Context ctx) {
            ctx.proceed();
        }
    };

    public static Middleware raw = new Middleware() {
        @Override
        public void handle(Request req, Response res, Context ctx) {
            ctx.proceed();
        }
    };

}
