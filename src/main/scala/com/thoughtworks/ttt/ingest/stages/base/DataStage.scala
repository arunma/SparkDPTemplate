package com.thoughtworks.ttt.ingest.stages.base

import com.thoughtworks.ttt.ingest.models.ErrorModels.{DataError, DataSetWithErrors}
import org.apache.spark.sql.Dataset

trait DataStage[T <: Dataset[_]] extends Serializable {
  def apply(errors: Dataset[DataError], data: T): (Dataset[DataError], T)
  def stage: String
}
