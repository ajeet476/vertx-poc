package com.example.starter.routes

import com.example.starter.kafka.MessagePublisher
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.tracing.opentracing.OpenTracingUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GrantRoute(private val messagePublisher: MessagePublisher) {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(HealthRoutes::class.java)
    const val AUTHORIZED = "authorized"
    const val AUTHORIZED_SUCCESS = "success"
    const val KAFKA_TOPIC = "grant-accept"
  }

  fun attachRoute(router: Router) {
    router.post("/grant")
      .handler { ctx -> authenticate(ctx) }
      .handler { ctx -> grant(ctx) }
  }

  private fun authenticate(ctx: RoutingContext) {
    LOG.info("validate request {}", ctx.request().path())
    val authHeader = ctx.request().getHeader("Authorization");
    if (authHeader == null || !authHeader.equals("test")) {
      ctx.response()
        .setStatusCode(401)
        .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
        .end(JsonObject().put("message", "unauthorized").toBuffer())
    }
    ctx.request().headers().add(AUTHORIZED, AUTHORIZED_SUCCESS)
    LOG.info("Span Info {}", OpenTracingUtil.getSpan())

    ctx.next()
  }

  private fun grant(ctx: RoutingContext) {
    LOG.info("return response {}", ctx.request().getHeader(AUTHORIZED))
    LOG.info("Span Info {}", OpenTracingUtil.getSpan())

    messagePublisher.publish(KAFKA_TOPIC, "Grant")

    ctx.response()
      .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
      .end(JsonObject().put("status", "grant").toBuffer())
  }
}
