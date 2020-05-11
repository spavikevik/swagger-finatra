package com.spavikevik.swagger_finatra

import com.twitter.finatra.http.annotations.Mustache
import com.twitter.finatra.http.marshalling.response._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import io.swagger.util.Json

@Mustache("index")
case class SwaggerView(title: String, path: String)

class SwaggerController(docPath: String = "/api-docs", finatraSwagger: FinatraSwagger) extends Controller {
  get(docPath) { _: Request =>
    val swagger = finatraSwagger.swagger

    response.ok.body(Json.mapper.writeValueAsString(swagger))
      .contentType("application/json").toFuture
  }

  get(s"${docPath}/ui") { _: Request =>
    val swagger = finatraSwagger.swagger

    val view = SwaggerView(swagger.getInfo.getTitle, docPath)
    response.ok.view("swagger-ui/index.mustache", view).toFuture
  }

  get(s"${docPath}/ui/:*") { request: Request =>
    val res = request.getParam("*")

    response.ok.file(s"public/swagger-ui/${res}")
  }
}
