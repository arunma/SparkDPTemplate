package com.thoughtworks.ttt.ingest.stages.base

import cats.data.Writer
import com.thoughtworks.ttt.ingest.models.ErrorModels.DataError
import com.thoughtworks.ttt.ingest.stages.base.DataStage.DatasetWithErrors
import org.apache.spark.sql.Dataset

//TODO encoder type
trait DataStage[T <: Dataset[_]] extends Serializable {
  def apply(data: T): DatasetWithErrors[T]
  def stage: String
}

object DataStage{
  type DatasetWithErrors[T] = Writer[Dataset[DataError], T]
}