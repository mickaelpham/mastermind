package mastermind.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> deployment) {
    var server = vertx.createHttpServer();

    var router = Router.router(vertx);
    router.route("/accounts/:accountId").handler(this::showAccount);

    server
        .requestHandler(router)
        .listen(
            8888,
            asyncResult -> {
              if (asyncResult.succeeded()) {
                log.info("deployed HTTP Server Verticle successfully on port 8888");
                deployment.complete();
              } else {
                log.error("an error happened when starting the HTTP server");
                deployment.fail(asyncResult.cause());
              }
            });
  }

  private void showAccount(RoutingContext context) {
    var accountId = context.request().getParam("accountId");

    vertx
        .eventBus()
        .request(
            "ruby.services.differ",
            "Nicolas Flamel (id: " + accountId + ")",
            asyncResult -> {
              if (asyncResult.succeeded()) {
                var response = (String) asyncResult.result().body();
                log.info("received reply from ruby verticle: " + response);
                context
                    .response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/vnd.api+json")
                    .end(response);
              } else {
                context.response().setStatusCode(500).end(asyncResult.cause().getMessage());
              }
            });
  }
}
