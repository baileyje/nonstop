import io.nonstop.core.App;
import io.nonstop.core.Request;
import io.nonstop.core.Response;
import io.nonstop.core.middleware.Context;
import io.nonstop.core.router.Router;

public class Main {

    static class TestMiddleware {
        static void handle(Request req, Response res, Context context) {
            System.out.println("Works I guess!!");
            context.proceed();
        }
    }


    public static void main(final String[] args) {
        final App app = new App();

        Router router = new Router();
        router.use(TestMiddleware::handle);
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
            res.send("Got It: " + req.path());
        });
        app.use("/api", router);
        app.listen(8080);
    }
}
