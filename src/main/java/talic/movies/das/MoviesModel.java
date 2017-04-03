package talic.movies.das;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import talic.movies.units.Movie;

/**
 *
 * @author Talic
 */
public class MoviesModel {

   /*
    -------------------------------
    * signup model method
    * param client 
    * param
    -------------------------------
    */
   public static void getMovies(JDBCClient client, Handler<JsonObject> handler) {

      client.getConnection(res -> {
         if (res.succeeded()) {

            SQLConnection connection = res.result();

            connection.query("SELECT * FROM movies", res2 -> {
               if (res2.succeeded()) {
                  handler.handle(res2.result().toJson());
               } else {
                  JsonObject err = new JsonObject().put("error", "message");
                  handler.handle(err);
               }

               connection.close();
            });
         } else {
            // Failed to get connection
            JsonObject err = new JsonObject().put("error", "message");
            handler.handle(err);
         }
      });
   }

   public static void getMovie(JDBCClient client, String id, Handler<JsonObject> handler) {
      client.getConnection(res -> {
         if (res.succeeded()) {
            SQLConnection connection = res.result();
            
            connection.queryWithParams("SELECT * FROM movies WHERE id=?", new JsonArray().add(id), res2 -> {
               if (res2.succeeded()) {
                  System.out.println(res2.result().toJson().encode());
                  handler.handle(res2.result().toJson());
               } else {
                  JsonObject err = new JsonObject().put("error", "message");
                  handler.handle(err);
               }

               connection.close();
            });
         } else {
            JsonObject err = new JsonObject().put("error", "message");
            handler.handle(err);
         }

      });
   }
   
   public static void deleteMovie(JDBCClient client, String id, Handler<JsonObject> handler) {
      client.getConnection(res -> {
         if (res.succeeded()) {
            SQLConnection connection = res.result();
            
            connection.updateWithParams("DELETE FROM movies WHERE id=?", new JsonArray().add(id), res2 -> {
               if (res2.succeeded()) {
                  System.out.println(res2.result().toJson().encode());
                  handler.handle(res2.result().toJson());
               } else {
                  JsonObject err = new JsonObject().put("error", "message");
                  handler.handle(err);
               }

               connection.close();
            });
         } else {
            JsonObject err = new JsonObject().put("error", "message");
            handler.handle(err);
         }

      });
   }
   
   public static void addMovie(JDBCClient client, Movie movie, Handler<JsonObject> handler) {
      JsonArray prop = new JsonArray().add(movie.getTitle()).add(movie.getLink());
      
      client.getConnection(res -> {
         if (res.succeeded()) {
            SQLConnection connection = res.result();
            
            connection.updateWithParams("INSERT INTO movies (title, link) VALUES (?, ?)", prop, res2 -> {
               if (res2.succeeded()) {
                  System.out.println(res2.result().toJson().encode());
                  handler.handle(res2.result().toJson());
               } else {
                  JsonObject err = new JsonObject().put("error", "message");
                  handler.handle(err);
               }

               connection.close();
            });
         } else {
            JsonObject err = new JsonObject().put("error", "message");
            handler.handle(err);
         }

      });
   }
   
   
}
