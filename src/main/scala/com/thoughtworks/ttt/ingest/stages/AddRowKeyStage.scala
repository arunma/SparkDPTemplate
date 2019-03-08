package com.thoughtworks.ttt.ingest.stages

import cats.data.Writer
import com.thoughtworks.ttt.config.PipelineConfig.DataColumn
import com.thoughtworks.ttt.ingest.StageConstants._
import com.thoughtworks.ttt.ingest.UDFs.generateUUID
import com.thoughtworks.ttt.ingest.models.ErrorModels.DataError
import com.thoughtworks.ttt.ingest.stages.base.DataStage
import com.thoughtworks.ttt.ingest.stages.base.DataStage.DatasetWithErrors
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, SparkSession}


class AddRowKeyStage(dataCols: List[DataColumn])(implicit spark: SparkSession, encoder: Encoder[DataError])
  extends DataStage[DataFrame] {

  override val stage: String = getClass.getSimpleName

  def apply(data: DataFrame): DatasetWithErrors[DataFrame] = {
    val colOrder = RowKey +: dataCols.map(_.name)
    val withRowKeyDf = data.withColumn(RowKey, lit(generateUUID())).cache()
    val returnDf = withRowKeyDf.select(colOrder.map(col): _*)
    Writer(spark.emptyDataset[DataError], returnDf)
  }
}
