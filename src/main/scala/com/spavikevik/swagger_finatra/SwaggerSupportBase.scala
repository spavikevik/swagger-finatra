package com.spavikevik.swagger_finatra

trait SwaggerSupportBase {
  def disableSwaggerOverride: Boolean = false
  val finatraSwagger: FinatraSwagger

  protected def registerOperation(path: String, method: String, operationWrap: OperationWrap): Unit = {
    if (!disableSwaggerOverride)
      finatraSwagger.registerOperation(path, method, operationWrap)
  }

  protected def swagger(f: OperationWrap => Unit): OperationWrap = {
    val op = new OperationWrap

    f(op)

    op
  }
}
