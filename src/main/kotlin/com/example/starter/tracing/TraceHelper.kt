package com.example.starter.tracing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

class TraceHelper {
  private companion object {
    val LOG: Logger = LoggerFactory.getLogger(TraceHelper::class.java)
    private const val MDC_SPAN_KEY = "request.span.id"
  }

  suspend fun <T> withContextTraced(
    context: CoroutineContext,
    reuseParentSpan: Boolean = true,
    block: suspend CoroutineScope.() -> T
  ): T {
    return coroutineScope {
      val spanElem = this.coroutineContext[SpanElement]

      if (spanElem == null) {
        LOG.warn ( "Calling 'withTracer', but no span found in context" )
        withContext(context, block)
      } else {
        val childSpan = if (reuseParentSpan) spanElem.span
        else spanElem.tracer.buildSpan("Req").asChildOf(spanElem.span).start()

        try {
          withContext(context + SpanElement(spanElem.tracer, childSpan), block)
        } finally {
          if (!reuseParentSpan) childSpan.finish()
        }
      }
    }
  }
}
