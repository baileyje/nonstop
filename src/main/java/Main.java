import io.nonstop.core.App;
import io.nonstop.core.Request;
import io.nonstop.core.Response;
import io.nonstop.core.middleware.Middleware;
import io.nonstop.core.middleware.Next;

public class Main {


    public static void main(final String[] args) {
        final App app = new App();

        app.use(new Middleware() {
            @Override
            public void handle(Request req, Response res, Next next) {
                res.send("Got It");
            }
        });
        app.listen(8080);
    }
}
