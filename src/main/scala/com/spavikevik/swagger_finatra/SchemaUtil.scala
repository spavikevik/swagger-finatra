package com.spavikevik.swagger_finatra

import io.swagger.models.{ArrayModel, Model, RefModel}
import io.swagger.models.properties.{ArrayProperty, Property, RefProperty}

object SchemaUtil {
  def toModel(schema: Property): Option[Model] = schema match {
    case p: RefProperty => Some(new RefModel(p.getSimpleRef))
    case p: ArrayProperty => Some({
      val arrayModel = new ArrayModel()
      arrayModel.setItems(p.getItems)
      arrayModel
    })
    case _ => None
  }
}