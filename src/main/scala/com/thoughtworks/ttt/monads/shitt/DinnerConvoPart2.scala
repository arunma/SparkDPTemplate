package com.thoughtworks.ttt.monads.shitt

import cats.Semigroup
import cats.data.Writer
//import cats.kernel.Semigroup
//import cats.implicits.catsKernelStdMonoidForString

object DinnerConvoPart2 extends App {


  //TODO

  def getHusbandConvo(decentFood: String): Writer[String, String] =
    Writer("This is food?", "Could you pass me the salt?")


  def getWifesConvo(saltyRequest: String): Writer[String, String] =
    Writer("Idiot!", "It's right there beside you")

  val convoWriter =
    for {
      saltyRequest      <-      getHusbandConvo("Pasta")
      thereYouGo        <-      getWifesConvo(saltyRequest)
    } yield thereYouGo


  val (mumblings, finalResult) = convoWriter.run


  println (s"Mumblings: $mumblings")
  println (s"Final result: $finalResult")

  //Hint: Show the presentation



}

