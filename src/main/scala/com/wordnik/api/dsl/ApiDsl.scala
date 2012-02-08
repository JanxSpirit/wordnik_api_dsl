package com.wordnik.api.dsl

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._

object ApiDsl extends JavaTokenParsers with ApiCalls {

  
  val word = regex("(?i)([A-Za-z]){0,20}".r)  
  val wordList = regex("([A-Za-z\\-0-9]){0,50}".r)

  val singleDefinition = regex("(?i)definition for".r)
  val multipleDefinitions = regex("(?i)definitions for".r)

  val list = regex("(?i)words in".r)
  val wordListsTerm = regex("(?i)word lists".r) | regex("(?i)wordlists".r)
  val wordListTerm =  regex("(?i)word list".r) |  regex("(?i)wordlist".r)
  val get = regex("(?i)get".r)

  val limit = regex("[1-9]".r) ^^ (limit => limit.toInt)
  
  val getLimit = get ~> limit

  val wordListWordsLimit = getLimit <~ list
  val wordDefinitionsLimit = getLimit <~ multipleDefinitions

  val wordListWords = get ~> list
  val wordDefinition = get ~> singleDefinition

  val defineWordMulti = wordDefinitionsLimit ~ word ^^ { 
    case limit ~ word => DefineCommand(Word(word), limit)
  }

  val defineWordSingle = wordDefinition ~> word ^^ (word => DefineCommand(Word(word)))

  val listWordListMulti = wordListWordsLimit ~ wordList ^^ { case limit ~ list => ListedWordsCommand(WordList(list), limit)}

  val listWordListSingle = wordListWords ~> wordList ^^ (list => ListedWordsCommand(WordList(list))) 

  val getWordLists = regex("(?i)get my word lists".r) ^^^ GetWordListsCommand

  val apiParser = defineWordMulti |
		  defineWordSingle | 
		  listWordListMulti | 
		  listWordListSingle

  def main(args: Array[String]) {
    Iterator.continually(Console.readLine).takeWhile(_ != "")
      .foreach(line => {
	println
	process(line)
      })
  }

  def process(s: String) = {
    parseAll(apiParser, s) match {
      case Success(res,_) => res match {
	case cmd: DefineCommand => {
	  println
	  getWordDefinition(cmd).foreach(println)
	  println
	}
	case cmd: ListedWordsCommand => {
	  println
	  getListedWords(cmd).foreach(println)
	  println
	}
      }
      case Failure(msg,_) => println("huh?")
      case Error(msg, _) => println("huh?")
    }
  }
}


