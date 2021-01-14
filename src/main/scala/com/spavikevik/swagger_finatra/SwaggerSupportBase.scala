package com.spavikevik.swagger_finatra

trait SwaggerSupportBase {
  val finatraSwagger: FinatraSwagger

  protected def registerOperation(path: String, method: String, operationWrap: OperationWrap): Unit = {
    finatraSwagger.registerOperation(path, method, operationWrap)
  }

  protected def swagger(f: OperationWrap => Unit): OperationWrap = {
    val op = new OperationWrap(finatraSwagger)

    f(op)

    op
  }
}
