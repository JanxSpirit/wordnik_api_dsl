package com.wordnik.api.dsl

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

object ApiDsl extends JavaTokenParsers with ApiCalls {

  
  val word = regex("([A-Za-z]){0,20}".r)
  
  val wordList = regex("([A-Za-z\\-0-9]){0,50}".r)

  val define = "define"  

  val list = "words in list"

  val limit = regex("[1-9]".r) ^^ (limit => limit.toInt)
  
  val getLimit = "get" ~> limit

  val wordListLimit = getLimit <~ list

  val defineWord = define ~> word ^^ (w => DefineCommand(Word(w)))

  val listWordList = wordListLimit ~ wordList ^^ { case limit ~ word => ListedWordsCommand(WordList(word), limit)}

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


