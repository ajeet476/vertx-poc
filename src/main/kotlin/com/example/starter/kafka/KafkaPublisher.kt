package com.example.starter.kafka

import io.vertx.core.Vertx
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord
import io.vertx.kafka.client.producer.impl.KafkaProducerRecordImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KafkaPublisher(vertx: Vertx) : MessagePublisher {
  private var producer:KafkaProducer<String, String>
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(KafkaPublisher::class.java)
  }

  init {
    val config = mapOf(
      "bootstrap.servers" to "localhost:9092",
      "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
      "value.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
      "acks" to "1",
    )

    producer = KafkaProducer.create(vertx, config)
  }

  override fun publish(topic: String, data: String) {
    LOG.info("will publish on topic {}", topic)
    val record: KafkaProducerRecord<String, String> = KafkaProducerRecordImpl(topic, data)
    producer.send(record) { result ->
      if (result.failed()) {
        LOG.error("failed to publish {}", result.result(), result.cause())
      }
      LOG.info("metadata {}", result.result())
    }
  }
}
