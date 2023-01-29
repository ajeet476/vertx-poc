package com.example.starter

import io.jaegertracing.internal.JaegerTracer
import io.jaegertracing.internal.reporters.RemoteReporter
import io.jaegertracing.internal.samplers.ProbabilisticSampler
import io.jaegertracing.thrift.internal.senders.UdpSender
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.tracing.opentracing.OpenTracingOptions

fun main() {
  val tracer = JaegerTracer.Builder("TestApp")
    .withReporter(RemoteReporter.Builder().withSender(UdpSender()).build())
    .withSampler(ProbabilisticSampler(1.0))
    .build()
  val options = VertxOptions().setTracingOptions(OpenTracingOptions(tracer))
  Vertx.vertx(options)
    .deployVerticle(MainVerticle())
    .onComplete { println("completed") }
    .onFailure { println("failed ${it.message} ${it.stackTrace}") }
}
