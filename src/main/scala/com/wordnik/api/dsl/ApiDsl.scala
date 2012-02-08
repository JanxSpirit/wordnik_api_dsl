package com.wordnik.api.dsl

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

object ApiDsl extends JavaTokenParsers {

  
  val word = regex("([A-Za-z]){0,20}".r)
  
  val wordList = regex("([A-Za-z\\-]){0,50}".r)

  val define = "define"  

  val list = "list"

  val defineWord = define ~> word ^^ (w => DefineCommand(Word(w)))

  val listWordList = list ~> wordList ^^ (w => ListCommand(WordList(w)))

  val apiParser = defineWord | listWordList

  def wordParser(s: String) = s.toList.map(caseInsensitiveLetter).reduceLeft((a,b) => a | b)

  def caseInsensitiveLetter(char: Char) = 
    char.toString.toUpperCase | 
    char.toString.toLowerCase

  def main(args: Array[String]) {
    Iterator.continually(Console.readLine).takeWhile(_ != "")
      .foreach(line => process(line))
  }

  def process(s: String) = {
    parseAll(apiParser, s) match {
      case Success(res,_) => res match {
	case DefineCommand(Word(w)) => println("you want to define %s".format(w))
	case ListCommand(WordList(p)) => println("you want to list wordList %s".format(p))
      }
      case Failure(msg,_) => println("huh?")
      case Error(msg, _) => println("huh?")
    }
  }
}


