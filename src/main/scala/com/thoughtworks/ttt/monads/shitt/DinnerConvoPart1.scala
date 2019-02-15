package com.thoughtworks.ttt.monads.shitt

import cats.data.Writer
import cats.implicits.catsKernelStdMonoidForList

object DinnerConvoPart1 extends App {

  def getHusbandConvo(decentFood: String): Writer[List[String], String] =
    Writer(List("This is food?", "God, take me already!"), "Could you pass me the salt?")


  def getWifeConvo(saltyRequest: String): Writer[List[String], String] =
    Writer(List("Idiot!"), "It's right there beside you")

  val convoWriter =
    for {
      saltyRequest      <-      getHusbandConvo("Pasta")
      thereYouGo        <-      getWifeConvo(saltyRequest)
    } yield thereYouGo


  val (mumblings, finalResult) = convoWriter.run

  println (s"Mumblings: $mumblings")
  println (s"Final result: $finalResult")

  //Hint: Magic?

}
