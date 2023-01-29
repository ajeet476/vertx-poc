package com.example.starter.routes

import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HealthRoutes {

  companion object {
    val LOG: Logger = LoggerFactory.getLogger(HealthRoutes::class.java)
  }

  fun attachRoute(router: Router) {
    router.get("/")
      .handler { ctx ->
        LOG.info("return response {}", ctx.request().path())
        ctx.response()
        .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
        .end(JsonObject().put("ping", "pong").toBuffer())
      }

    router.get("/health")
      .handler { ctx ->
        LOG.info("return response {}", ctx.request().path())

        ctx.response()
        .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
        .end(JsonObject().put("status", "up").put("components", listOf("liveness", "readiness")).toBuffer()) }

    router.get("/health/liveness")
      .handler { ctx ->
        LOG.info("return response {}", ctx.request().path())
        ctx.response()
        .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
        .end(JsonObject().put("status", "up").toBuffer()) }
  }
}
