package mastermind;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    var server = vertx.createHttpServer();

    server.requestHandler(request -> {
      request.response().end("Hello, World!");
    });

    server.listen(8888, ar -> {
      if (ar.succeeded()) {
        log.info("successfully started HTTP server at localhost:8888");
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
