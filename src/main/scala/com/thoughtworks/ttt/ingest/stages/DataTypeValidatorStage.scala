package com.thoughtworks.ttt.ingest.stages

import cats.data.Writer
import com.thoughtworks.ttt.config.PipelineConfig.DataColumn
import com.thoughtworks.ttt.ingest.StageConstants._
import com.thoughtworks.ttt.ingest.UDFs.validateRowUDF
import com.thoughtworks.ttt.ingest.models.ErrorModels.DataError
import com.thoughtworks.ttt.ingest.stages.base.DataStage
import com.thoughtworks.ttt.ingest.stages.base.DataStage.DatasetWithErrors
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

class DataTypeValidatorStage(dataCols: List[DataColumn])(implicit val spark: SparkSession) extends DataStage[DataFrame] {

  override val stage = getClass.getSimpleName

  def apply(data: DataFrame): DatasetWithErrors[DataFrame] = {

    val withErrorsDF = data.withColumn(RowLevelErrorListCol, validateRowUDF(dataCols, stage)(struct(data.columns.map(data(_)): _*)))

    import spark.implicits._

    val errorRecords =
      withErrorsDF
        .select(RowLevelErrorListCol)
        .select(explode(col(RowLevelErrorListCol)))
        .select("col.*")
        .map(row => DataError(row))

    Writer(errorRecords, withErrorsDF.drop(RowLevelErrorListCol))
  }
}