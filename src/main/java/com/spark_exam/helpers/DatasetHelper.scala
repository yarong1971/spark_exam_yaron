package com.spark_exam.helpers

import org.apache.spark.sql._

import scala.reflect.runtime.universe.TypeTag

object DatasetHelper {
  implicit class DatasetExtension(dataset: Dataset[Row]) {
    def toList[T <: Product : TypeTag](): java.util.List[T] = {
      val encoder: Encoder[T] = Encoders.product[T]
      dataset.as[T](encoder).collectAsList()
    }
  }
}
