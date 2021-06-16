package com.spark_exam.helpers

import com.spark_exam.models.GameStat
import org.apache.spark.sql._

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.reflect.runtime.universe.TypeTag

object DatasetHelper {
  implicit class DatasetExtension(dataset: Dataset[Row]) {
    def toList[T <: Product : TypeTag](): java.util.List[T] = {
      val encoder: Encoder[T] = Encoders.product[T]
      dataset.as[T](encoder).collectAsList()
    }
  }
}
