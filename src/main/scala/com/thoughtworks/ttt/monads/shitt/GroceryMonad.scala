package com.thoughtworks.ttt.monads.shitt

object GroceryMonad extends App {

  def getFathersStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "shaving"    =>    Option("Razor")
    case "beer"       =>    Option("GetWastedQuickBeer")
    case _            =>    None
  }

  def getMothersStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "chocolates" =>    Option("TheExpensiveKind")
    case "wine"       =>    Option("ForgetMistakesOfLife")
    case _            =>    None
  }

  def getKidsStuff(kind: String): Option[String] = kind.toLowerCase match {
    case "toy"        =>    Option("ThatTruckIsHuge")
    case "pencil"     =>    Option("MillionthPencil")
    case _            =>    None
  }

  val first =
    getFathersStuff("beer").flatMap { f =>
      getMothersStuff("wine").flatMap { m =>
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