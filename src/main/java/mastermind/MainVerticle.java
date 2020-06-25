package mastermind;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> serviceDeployment) {
    Promise<Void> httpServerDeployed = Promise.promise();
    Promise<Void> rubyVerticleDeployed = Promise.promise();

    vertx.deployVerticle(
        "mastermind.http.ServerVerticle",
        asyncResult -> {
          if (asyncResult.succeeded()) {
            httpServerDeployed.complete();
          } else {
            httpServerDeployed.fail(asyncResult.cause());
          }
        });

    vertx.deployVerticle(
        "ruby/my_verticle.rb",
        asyncResult -> {
          if (asyncResult.succeeded()) {
            rubyVerticleDeployed.complete();
          } else {
            rubyVerticleDeployed.fail(asyncResult.cause());
          }
        });

    CompositeFuture.all(httpServerDeployed.future(), rubyVerticleDeployed.future())
        .onComplete(
            allVerticlesDeployment -> {
              log.info("all verticles have been deployed successfully");
              if (allVerticlesDeployment.succeeded()) {
                serviceDeployment.complete();
              } else {
                log.error("an error occurred while deploying one or more verticles");
                serviceDeployment.fail(allVerticlesDeployment.cause());
              }
            });
  }
}
