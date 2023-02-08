package com.example.starter.routes

import com.example.starter.kafka.MessagePublisher
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

class MainRoute {
  companion object {
    const val JSON = "application/json"
    const val CONTENT_TYPE = "content-type"
  }

  fun routes(vertx: Vertx, messagePublisher: MessagePublisher): Router {
    val router = routes(vertx)
    GrantRoute(messagePublisher).attachRoute(router)

    return router
  }

  fun routes(vertx: Vertx): Router {
    val router = Router.router(vertx)
    createBodyHandlers(router)
    HealthRoutes().attachRoute(router)
    UserRoutes().attachRoute(router)

    return router
  }

  private fun createBodyHandlers(router: Router) {
    router.post().handler(BodyHandler.create())
    router.put().handler(BodyHandler.create())
    router.patch().handler(BodyHandler.create())
  }
}
