package com.spavikevik.swagger_finatra

import io.swagger.models.{Model, Operation, Response}
import io.swagger.models.parameters._
import io.swagger.util.Json

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe._
import com.spavikevik.swagger_finatra.SchemaUtil._

class OperationWrap(finatraSwagger: FinatraSwagger) {
  private[swagger_finatra] val operation = new Operation

  def addSecurity(s: String, scopes: List[String]): Unit = {
    operation.addSecurity(s, scopes.asJava)
  }

  def summary(value: String): Unit = {
    operation.summary(value)
  }

  def tags(values: String*): Unit = {
    operation.setTags(values.asJava)
  }

  def routeParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit = {
    val param: Parameter = new PathParameter()
      .name(name)
      .description(description)
      .required(required)
      .property(finatraSwagger.registerModel[T].orNull)

    operation.parameter(param)
  }

  def queryParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit = {
    val param: Parameter = new QueryParameter()
      .name(name)
      .description(description)
      .required(required)
      .property(finatraSwagger.registerModel[T].orNull)

    operation.parameter(param)
  }

  def headerParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit = {
    val param: Parameter = new HeaderParameter()
      .name(name)
      .description(description)
      .required(required)
      .property(finatraSwagger.registerModel[T].orNull)

    operation.parameter(param)
  }

  def formParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit = {
    val param: Parameter = new FormParameter()
      .name(name)
      .description(description)
      .required(required)
      .property(finatraSwagger.registerModel[T].orNull)

    operation.parameter(param)
  }

  def cookieParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit = {
    val param: Parameter = new CookieParameter()
      .name(name)
      .description(description)
      .required(required)
      .property(finatraSwagger.registerModel[T].orNull)

    operation.parameter(param)
  }

  def bodyParam[T: TypeTag](name: String, description: String = "", example: Option[T] = None): Unit = {
    val model: Option[Model] = finatraSwagger.registerModel[T].flatMap(toModel)

    example.foreach { e: T =>
      model.foreach(_.setExample(Json.mapper.writeValueAsString(e)))
    }

    val param: Parameter = new BodyParameter()
      .name(name)
      .description(description)
      .schema(model.orNull)

    operation.parameter(param)
  }

  def response[T: TypeTag](status: Int, description: String = "", example: Option[T] = None): Unit = {
    val model: Option[Model] = finatraSwagger.registerModel[T].flatMap(toModel)

    example.foreach { e: T =>
      model.foreach(_.setExample(e))
    }

    val response: Response = new Response()
      .description(description)
      .responseSchema(model.orNull)

    operation.response(status, response)
  }

  def description(value: String): Unit = {
    operation.setDescription(value)
  }

  def consumes(values: String*): Unit = {
    operation.setConsumes(values.asJava)
  }

  def produces(values: String*): Unit = {
    operation.setProduces(values.asJava)
  }

  def deprecated(value: Boolean): Unit = {
    operation.deprecated(value)
  }
}
