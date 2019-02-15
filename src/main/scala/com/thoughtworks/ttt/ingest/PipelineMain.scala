package com.thoughtworks.ttt.ingest

import cats.syntax.either._
import com.thoughtworks.ttt.config.PipelineConfig
import com.thoughtworks.ttt.config.PipelineConfig.{DatasetConfig, Delimited}
import com.thoughtworks.ttt.ingest.models.ErrorModels.DataError
import com.thoughtworks.ttt.ingest.stages.{AddRowKeyStage, DataTypeValidatorStage}
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object PipelineMain {

  def main(args: Array[String]): Unit = {
    val filePath = args(0)

    val sparkConf = buildSparkConf()

    implicit val spark = SparkSession
      .builder()
      .config(sparkConf)
      .appName("Boring Pipeline")
      .master("local[*]")
      .getOrCreate()

    LogManager.getRootLogger.setLevel(Level.WARN)
    spark.sparkContext.setLogLevel("ERROR")

    val fileName = StringUtils.substringAfterLast(filePath, "/")
    val dataSetE =
      for {
        ingestionConfig <- PipelineConfig.getIngestionConfig(
          getClass.getResource("/datasets_main.conf"), "ingestionConfig")
        dataSetIn <- PipelineConfig.findDatasetFromFileName(fileName, ingestionConfig) //Need to figure out how to remove the filename in StructuredStreaming
      } yield dataSetIn


    if (dataSetE.isLeft) {
      sys.error("No matching dataset configuration found for the given file. Please check the filePattern configuration for each dataset")
    }

    dataSetE.map(config => runPipeline(filePath, config))

  }

  private def runPipeline(filePath: String, dataSetConfig: DatasetConfig)(implicit spark: SparkSession) = {
    val sourceRawDf =
      spark
        .read
        .format("csv")
        .option("header", true)
        .option("delimiter", dataSetConfig.fileFormat.asInstanceOf[Delimited].delimiter) //FIXME There must be a better way to do this
        .load(filePath)

    import DataFrameOps._

    val dataCols = dataSetConfig.columns

    val initErrors = spark.emptyDataset[DataError]

    val pipelineStages = List(
      new AddRowKeyStage(dataCols),
      new DataTypeValidatorStage(dataCols)
    )
    val init = (spark.emptyDataset[DataError], sourceRawDf)

    val (errors, processedDf) = pipelineStages.foldLeft(init) { case ((accumErr, df), stage) =>
      stage(accumErr, df)
    }

    processedDf.show(false)
    errors.show(false)
  }


  def buildSparkConf(): SparkConf = new SparkConf()
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false")
    .set("spark.sql.streaming.schemaInference", "true")
    .set("spark.sql.autoBroadcastJoinThreshold", "-1")
    .set("spark.scheduler.mode", "FAIR")
    .set("spark.sql.shuffle.partitions", "1")
    .set("spark.default.parallelism", "1")
    .set("spark.sql.warehouse.dir", "/tmp/awaywarehose")
    .set("spark.kryoserializer.buffer.max", "1g")
}
