package com.example.starter.kafka

import com.example.starter.routes.GrantRoute
import io.vertx.core.Vertx
import io.vertx.kafka.client.consumer.KafkaConsumer
import io.vertx.kafka.client.consumer.KafkaConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KafkaConsumer(vertx: Vertx) {
  private var consumer:KafkaConsumer<String, String>
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(KafkaConsumer::class.java)
  }

  init {
    val config = mapOf(
      "bootstrap.servers" to "localhost:9092",
      "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
      "group.id" to "my_group",
      "auto.offset.reset" to "earliest",
      "enable.auto.commit" to "false"
    )

    consumer = KafkaConsumer.create(vertx, config)

    consumer.subscribe(GrantRoute.KAFKA_TOPIC)
    consumer.handler{ record -> handleGrant(record)}
  }

  private fun handleGrant(record: KafkaConsumerRecord<String, String>) {
    LOG.info("processing record {}", record.topic())
    LOG.info("Span Info {}", record.headers())
    LOG.info("processing record {}", record.value())
  }
}
