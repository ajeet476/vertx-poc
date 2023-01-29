package com.example.starter.kafka

interface MessagePublisher {
  fun publish(topic: String, data: String)
}
