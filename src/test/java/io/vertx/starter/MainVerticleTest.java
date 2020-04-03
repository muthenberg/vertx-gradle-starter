package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {

  @BeforeEach
  void prepare(Vertx vertx, VertxTestContext testContext) {
    System.out.println("Begin prepare");
    vertx.deployVerticle(MainVerticle.class.getCanonicalName(), testContext.succeeding(id -> testContext.completeNow()));
    System.out.println("End prepare");
  }

  @AfterEach
  void after() {
    System.out.println("after");
  }

  @Test
  @DisplayName("Check that the server has started")
  void checkServerHasStarted(Vertx vertx, VertxTestContext testContext) {
    System.out.println("Begin test");
    WebClient webClient = WebClient.create(vertx);
    webClient.get(8080, "localhost", "/")
      .as(BodyCodec.string())
      .send(testContext.succeeding(response -> /*testContext.verify(() -> {
        System.out.println("verifying");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().length() > 0);
        assertTrue(response.body().contains("Hello Vert.x!"));
        testContext.completeNow();
      })*/{
        webClient.get(8080, "localhost", "/").send(response2 -> {
            System.out.println("response 2");
            testContext.verify(()-> {
              assertEquals(200, response2.result().statusCode());
              testContext.completeNow();
            });
          }
          );
      }));
    System.out.println("End test");
  }
}
