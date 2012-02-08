package com.wordnik.api.dsl

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

object ApiDsl extends JavaTokenParsers with ApiCalls {

  
  val word = regex("([A-Za-z]){0,20}".r)
  
  val wordList = regex("([A-Za-z\\-0-9]){0,50}".r)

  val define = "get definition for"  

  val list = "list"

  val defineWord = define ~> word ^^ (w => DefineCommand(Word(w)))

  val listWordList = list ~> wordList ^^ (w => ListedWordsCommand(WordList(w)))

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
	case cmd: DefineCommand => {
	  getWordDefinition(cmd).foreach(println)
	}
	case cmd: ListedWordsCommand => {
	  getListedWords(cmd).foreach(println)
	}
      }
      case Failure(msg,_) => println("huh?")
      case Error(msg, _) => println("huh?")
    }
  }
}


