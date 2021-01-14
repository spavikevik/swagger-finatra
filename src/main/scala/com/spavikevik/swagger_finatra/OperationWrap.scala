package com.spavikevik.swagger_finatra

import io.swagger.models.{Model, Operation, Response}
import io.swagger.models.parameters._
import io.swagger.util.Json

import scala.collection.JavaConverters._
import scala.reflect.runtime.universe._
import com.spavikevik.swagger_finatra.SchemaUtil._

class OperationWrap(finatraSwagger: FinatraSwagger) {
  trait ParamWithType {
    type T
    def _name: String
    def _description: String
    def _required: Boolean
  }

  trait BodyParamWithType {
    type T
    def _name: String
    def _description: String
    def _example: Option[T]
  }

  trait ResponseWithType {
    type T
    def _status: Int
    def _description: String
    def _example: Option[T]
  }
  
  private var security: Map[String, List[String]] = Map.empty
  private var summaryText: Option[String] = None
  private var tagsList: Seq[String] = Seq.empty
  private var routeParams: List[ParamWithType] = List.empty
  private var queryParams: List[ParamWithType] = List.empty
  private var headerParams: List[ParamWithType] = List.empty
  private var formParams: List[ParamWithType] = List.empty
  private var cookieParams: List[ParamWithType] = List.empty
  private var bodyParams: List[BodyParamWithType] = List.empty
  private var responses: List[ResponseWithType] = List.empty
  private var descriptionText: Option[String] = None
  private var consumesList: Seq[String] = Seq.empty
  private var producesList: Seq[String] = Seq.empty
  private var isDeprecated: Boolean = false

  def addSecurity(s: String, scopes: List[String]): Unit =
    security = security + (s -> scopes)

  def summary(value: String): Unit =
    summaryText = Some(value)

  def tags(value: String*): Unit =
    tagsList = value

  def routeParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit =
    routeParams = appendParam[T](routeParams)(name, description, required)

  def queryParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit =
    queryParams = appendParam[T](queryParams)(name, description, required)

  def headerParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit =
    headerParams = appendParam[T](headerParams)(name, description, required)

  def formParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit =
    formParams = appendParam[T](formParams)(name, description, required)

  def cookieParam[T: TypeTag](name: String, description: String = "", required: Boolean = true): Unit =
    cookieParams = appendParam[T](cookieParams)(name, description, required)

  def bodyParam[TT: TypeTag](name: String, description: String = "", example: Option[TT] = None): Unit =
    bodyParams = bodyParams :+ new BodyParamWithType {
      type T = TT
      def _name: String = name
      def _example: Option[T] = example
      def _description: String = description
    }

  def response[TT: TypeTag](status: Int, description: String = "", example: Option[TT] = None): Unit =
    responses = responses :+ new ResponseWithType {
      type T = TT
      def _status: Int = status
      def _example: Option[T] = example
      def _description: String = description
    }

  def description(value: String): Unit =
    descriptionText = Some(value)

  def consumes(values: String*): Unit =
    consumesList = values

  def produces(values: String*): Unit =
    producesList = values

  def deprecated(value: Boolean): Unit =
    isDeprecated = value

  def toOperation: Operation = {
    val operation = new Operation
    
    security.foreach { s => operation.addSecurity(s._1, s._2.asJava) }
    summaryText.foreach(operation.setSummary)
    operation.setTags(tagsList.asJava)

    queryParams.foreach { p =>
      val param: Parameter = new QueryParameter()
        .name(p._name)
        .description(p._description)
        .required(p._required)
        .property(finatraSwagger.registerModel[p.T].orNull)

      operation.parameter(param)
    }

    headerParams.foreach { p =>
      val param: Parameter = new HeaderParameter()
        .name(p._name)
        .description(p._description)
        .required(p._required)
        .property(finatraSwagger.registerModel[p.T].orNull)

      operation.parameter(param)
    }

    formParams.foreach { p =>
      val param: Parameter = new FormParameter()
        .name(p._name)
        .description(p._description)
        .required(p._required)
        .property(finatraSwagger.registerModel[p.T].orNull)

      operation.parameter(param)
    }

    cookieParams.foreach { p =>
      val param: Parameter = new CookieParameter()
        .name(p._name)
        .description(p._description)
        .required(p._required)
        .property(finatraSwagger.registerModel[p.T].orNull)

      operation.parameter(param)
    }

    bodyParams.foreach { p =>
      val model: Option[Model] = finatraSwagger.registerModel[p.T].flatMap(toModel)

      p._example.foreach { e: p.T =>
        model.foreach(_.setExample(Json.mapper.writeValueAsString(e)))
      }

      val param: Parameter = new BodyParameter()
        .name(p._name)
        .description(p._description)
        .schema(model.orNull)

      operation.parameter(param)
    }

    responses.foreach { r =>
      val model: Option[Model] = finatraSwagger.registerModel[r.T].flatMap(toModel)

      r._example.foreach { e: r.T =>
        model.foreach(_.setExample(e))
      }

      val response: Response = new Response()
        .description(r._description)
        .responseSchema(model.orNull)

      operation.response(r._status, response)
    }
    
    descriptionText.foreach(operation.setDescription)
    operation.setConsumes(consumesList.asJava)
    operation.setProduces(producesList.asJava)
    operation.setDeprecated(isDeprecated)
    
    operation
  }

  private def appendParam[TT: TypeTag](params: List[ParamWithType])(
    name: String,
    description: String = "",
    required: Boolean = true
  ): List[ParamWithType] = params :+ new ParamWithType {
    type T = TT
    def _name: String = name
    def _required: Boolean = required
    def _description: String = description
  }
}