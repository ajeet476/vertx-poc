package com.example.starter.routes

import com.example.starter.routes.user.UserDto
import com.example.starter.routes.user.UserView
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class UserRoutes {
  companion object {
    val LOG: Logger = LoggerFactory.getLogger(UserRoutes::class.java)
    val jsonMapper: ObjectMapper = ObjectMapper()
  }

  fun attachRoute(router: Router) {
    router.get("/users")
      .handler { ctx ->
        val views : Set<UserView> = ctx.request().getParam("view", "default")
          .split(",")
          .map { it.uppercase(Locale.getDefault()) }
          .map { UserView.valueOf(it) }
          .toSet()
        LOG.info("return response {} with views {}", ctx.request().path(), views)

        ctx.response()
        .putHeader(MainRoute.CONTENT_TYPE, MainRoute.JSON)
        .end(userView(views))
      }
  }

  private fun userView(views: Set<UserView>) : String {
    val dto = UserDto(
      id = UUID.randomUUID(),
      status = true,
      kycStatus = false,
      grantStatus = false,
      migrationStatus = Random().nextBoolean(),
      memberId = Random().nextLong(),
      note = ""
    )

    val view = views.first().getView()::class
    LOG.info("return response {}", view)

    return jsonMapper
      .writerWithView(view.java)
      .writeValueAsString(dto)
  }
}
