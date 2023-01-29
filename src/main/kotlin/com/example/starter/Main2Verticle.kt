package com.example.starter

import com.example.starter.kafka.KafkaConsumer
import com.example.starter.kafka.KafkaPublisher
import com.example.starter.routes.GrantRoute
import com.example.starter.tracing.SpanElement
import com.example.starter.tracing.TraceHelper
import io.opentracing.Span
import io.opentracing.Tracer
import io.vertx.core.Handler
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.tracing.opentracing.OpenTracingUtil
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Main2Verticle(private val tracer: Tracer) : CoroutineVerticle() {

  companion object {
    val LOG: Logger = LoggerFactory.getLogger(Main2Verticle::class.java)
    private const val MDC_SPAN_KEY = "request.span.id"
  }

  override suspend fun start() {
    val router = Router.router(vertx)
    KafkaConsumer(vertx)
    val kafkaPublisher = KafkaPublisher(vertx)

    router.post("/grant")
      .coroutineHandler { ctx ->
        kafkaPublisher.publish(GrantRoute.KAFKA_TOPIC, "...sending...")
        ctx.end("Hello from coroutine handler")
      }

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .await()
  }

  private fun Route.coroutineHandler(handler: Handler<RoutingContext>): Route = this.coroutineHandler(handler::handle)

  private fun Route.coroutineHandler(handler: suspend (RoutingContext) -> (Unit)): Route = handler { ctx ->
    val span: Span = OpenTracingUtil.getSpan()
    LOG.info("span {}", span)
    launch(ctx.vertx().dispatcher() + SpanElement(tracer, span)) {            // (6)
      TraceHelper().withContextTraced(coroutineContext) {
        try {
          handler(ctx)
        } catch (t: Throwable) {
          ctx.fail(t)
        }
      }
    }
  }
}
