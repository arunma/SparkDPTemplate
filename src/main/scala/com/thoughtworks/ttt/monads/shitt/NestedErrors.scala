package com.thoughtworks.ttt.monads.shitt

import com.thoughtworks.ttt.config.PipelineConfig.DataColumn
import com.thoughtworks.ttt.ingest.StageConstants._
import com.thoughtworks.ttt.ingest.models.ErrorModels.DataError
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.ArrayType

object NestedErrors extends App {

 /* implicit def spark = SparkSession.builder()
      .appName("Test Context")
      .config(new SparkConf())
      .master("local[1]")
      .getOrCreate()

  val sqlContext = spark.sqlContext
  import sqlContext.implicits._

  spark.sparkContext.setLogLevel("ERROR")

  val df = spark
    .sparkContext
    .parallelize(List(
      ("r2", "x", "t", "5")
    ))
    .toDF(RowKey, "name", "age", "gpa")


  val errorUdf1 = udf (() =>
    DataError("r2", "DataTypeValidatorStage", "name", "t", "Value could not be converted to the target datatype 'string'. Error: For input string: \"t\". Column config : DataColumn(name,string,,true,false).", "ErrorSeverity", "")
  )

  val errorUdf2 = udf (() =>
    DataError("r2", "DataTypeValidatorStage", "name", "v", "Value could not be converted to the target datatype 'string'. Error: For input string: \"v\". Column config : DataColumn(name,string,,true,false).", "ErrorSeverity", "")
  )

  val arrayType = array(errorUdf1(), errorUdf2())

  /*
  DataError("r4", "DataTypeValidatorStage", "name", "v", "Value could not be converted to the target datatype 'string'. Error: For input string: \"v\". Column config : DataColumn(name,string,,true,false).", "ErrorSeverity", ""),
    DataError("r4", "DataTypeValidatorStage", "age", "u", "Value could not be converted to the target datatype 'long'. Error: For input string: \"u\". Column config : DataColumn(age,long,,true,false).", "ErrorSeverity", "")
   */


  /*val withErrDfTemp = df.withColumn("errors_1", errorUdf1()).withColumn("errors_2", errorUdf2())

  val errorsDf = withErrDfTemp.groupBy("rowKey").agg(collect_list(struct("errors_1", "errors_2")))
  //val withErrDf = withErrDfTemp.withColumn("errors", )

  val unionErrorDf = withErrDfTemp.select("errors_1").union(withErrDfTemp.select("errors_2"))
  unionErrorDf.show(false)

  val finalDf = withErrDfTemp.drop("errors_1").drop("errors_2").join(unionErrorDf, $"rowKey"===$"errors_1.rowKey")
  finalDf.printSchema()*/

  val finalDf = df.withColumn("errors", arrayType)


  trait Semigroup[A]{
    def combine(x: A, y: A): A
  }

  trait Semigroup[String]{
    def combine(x: String, y: String): String
  }

  implicit val stringSemiGroupImpl = new Semigroup[String] {
      override def combine(x: String, y: String): String = x + y
  }

  trait Monoid extends Semigroup[A]{

  }




  finalDf.printSchema()
  finalDf.show(false)
*/



  def toString(double: Double): Option[String] =
    if (double < 1.0) None
    else Some(s"String: ${double.toString}")

  println (Option(1).flatMap(x => toString(x)))


}
