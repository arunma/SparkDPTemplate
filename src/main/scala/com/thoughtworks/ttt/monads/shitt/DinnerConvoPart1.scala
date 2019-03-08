package com.thoughtworks.ttt.monads.shitt

import cats.data.{Writer}
import cats.kernel.Semigroup
//import cats.implicits.catsKernelStdMonoidForList

object DinnerConvoPart1 extends App {


  implicit def semiGroupList[A] = new Semigroup[List[A]] {
    override def combine(x: List[A], y: List[A]): List[A] = x:::y
  }

  def getHusbandConvo(decentFood: String): Writer[List[String], String] =
    Writer(
      List("The Food is terrible!", "God, take me already!"), //Log part
      "Could you pass me the salt?"                           //Value part
    )

  def getWifeConvo(saltyRequest: String): Writer[List[String], String] =
    Writer(
      List("Idiot!"),
      "It's right there beside you"
    )

  val convoWriter: Writer[List[String], String] =
    for {
      saltyRequest      <-      getHusbandConvo("Pasta")
      thereYouGo        <-      getWifeConvo(saltyRequest)
    } yield thereYouGo


  val (mumblings, finalResult) = convoWriter.run

  println (s"Mumblings: $mumblings")
  println (s"Final result: $finalResult")

  //Hint: Magic?

}
