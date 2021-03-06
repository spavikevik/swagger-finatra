package com.spavikevik.swagger_finatra

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.google.inject.Module
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.modules.MustacheModule
import com.twitter.finatra.http.routing.HttpRouter
import io.swagger.models.auth.BasicAuthDefinition
import io.swagger.models.{ExternalDocs, Tag}
import io.swagger.util.Json

object SampleSwagger extends FinatraSwagger {
  Json.mapper().setPropertyNamingStrategy(new SnakeCaseStrategy)
}

object SampleApp extends HttpServer {
  override val modules: Seq[Module] = Seq(MustacheModule)

  SampleSwagger
    .registerInfo(
      description = "The Student / Course management API, this is a sample for swagger document generation",
      version = "1.0.1",
      title = "Student / Course Management API")
    .addSecurityDefinition("sampleBasic", {
      val d = new BasicAuthDefinition()
      d.setType("basic")
      d
    })
    .addTag(new Tag()
      .name("Student")
      .description("Everything about student")
      .externalDocs(new ExternalDocs()
        .url("http://example.com/examle")
        .description("this is external doc")
      )
    )
    .setExternalDocs({
      val d = new ExternalDocs()
      d.setDescription("This is external document")
      d.setUrl("https://github.com/spavikevik/swagger-finatra")
      d
    })


  override def configureHttp(router: HttpRouter) {
    router
      .filter[CommonFilters]
      .add(new SwaggerController(finatraSwagger = SampleSwagger))
      .add[SampleController]
  }
}
