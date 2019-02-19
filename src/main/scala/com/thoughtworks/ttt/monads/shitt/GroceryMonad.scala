package com.thoughtworks.ttt.monads.shitt

object GroceryMonad extends App {

  def getFathersStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "shaving"    =>    Option("Razor")
    case _            =>    None
  }

  def getMothersStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "chocolates" =>    Option("TheExpensiveKind")
    case _            =>    None
  }

  def getKidsStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "toy"        =>    Option("ThatTruckIsHuge")
    case _            =>    None
  }

  //Hint: Monads can be used for sequencing computations
  val first: Option[String] =
    getFathersStuff("shaving").flatMap { f =>
      getMothersStuff("chocolates").flatMap { m =>
        getKidsStuff("toy").flatMap { k =>
          Option(s"$f, $m, $k")
        }
      }
    }

  println (s"First $first")

  //TODO secondMonad

}





/*import scala.reflect.runtime.universe._

val result = show{
  reify{
    for {
      m <- getMothersStuff("chocolates")
      f <- getFathersStuff("shaving")
      k <- getKidsStuff("pencil")
    } yield Option(s"$m, $f, $k")

  }
}

println (result)*/