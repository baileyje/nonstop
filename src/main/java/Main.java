import io.nonstop.core.App;
import io.nonstop.core.middleware.BodyParser;
import io.nonstop.core.router.Router;
import io.nonstop.core.util.data.DataNode;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(final String[] args) {
        final App app = new App();
        final Router router = new Router();
        router.use(BodyParser.json);
        router.use((req, res, context) -> {
            System.out.println("MW1");
            context.proceed();
        });
        router.use((req, res, context) -> {
            System.out.println("MW2");
            context.proceed();
        });
        router.use((req, res, context) -> {
            System.out.println("MW3");
            context.proceed();
        });
        router.get("/test", (req, res, context) -> {
            Map<String, String> data = new HashMap<String, String>() {{
                put("test1", "value1");
                put("test2", "value2");
            }};
            for (DataNode node : req.body()) {
                System.out.println(node.asString());
            }
            res.json(data);
        });
        app.use("/api", router);
        app.listen(8080);
    }
}
