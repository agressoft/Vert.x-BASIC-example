package talic.movies;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import talic.movies.das.MoviesModel;
import talic.movies.units.Movie;

/**
 *
 * @author Talic
 */
public class Main extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Main.class.getName(),
                new DeploymentOptions().setInstances(1));
    }

    @Override
    public void start() {

        final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:mysql://localhost/movies")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("user", "root")
                .put("password", "")
                .put("max_pool_size", 20));

        Router router = Router.router(vertx);

        router.route("/static/*").handler(StaticHandler.create("webroot"));

        // enable body handler so we can use request body param
        router.route("/*").handler(BodyHandler.create());

        // set landing path
        router.route("/").handler(routingContext -> {
            routingContext.response().sendFile("webroot/index.html");
        });

        // switch to another page
        router.route("/watch/:id").handler(routingContext -> {
            routingContext.response().sendFile("webroot/watch.html");
        });

        // REST endpoints
        router.get("/movies").handler(routingContext -> {
            MoviesModel.getMovies(client, res -> {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encode(res));
            });
        });
        
        
        router.get("/movies/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");
            MoviesModel.getMovie(client, id, res -> {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encode(res));
            });
        });
        
       
        router.delete("/movies/:id").handler(routingContext -> {
            String id = routingContext.request().getParam("id");
            MoviesModel.deleteMovie(client, id, res -> {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encode(res));
            });
        });
        
        
        router.post("/movies").handler(routingContext -> {
            final Movie movie = Json.decodeValue(routingContext.getBodyAsString(), Movie.class);
            MoviesModel.addMovie(client, movie, res -> {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encode(res));
            });
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        System.out.println("Server has started!");
    }
}
