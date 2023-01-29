package com.example.starter

import com.example.starter.kafka.KafkaConsumer
import com.example.starter.kafka.KafkaPublisher
import com.example.starter.routes.MainRoute
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainVerticle : AbstractVerticle() {

  companion object {
    val LOG: Logger = LoggerFactory.getLogger(MainVerticle::class.java)
  }

  override fun start(startPromise: Promise<Void>) {
    KafkaConsumer(vertx)
    val kafkaPublisher = KafkaPublisher(vertx)
    vertx.setPeriodic(50000) {
      kafkaPublisher.publish("my-tpic", "Data")
    }

    vertx
      .createHttpServer()
      .requestHandler(MainRoute().routes(vertx, kafkaPublisher))
      .exceptionHandler { println(it) }
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          LOG.info("HTTP server started on port 8888")
        } else {
          LOG.warn("HTTP server failed", http.cause())
          startPromise.fail(http.cause());
        }
      }
  }


}
