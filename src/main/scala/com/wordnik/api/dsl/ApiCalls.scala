package com.wordnik.api.dsl

import dispatch._
import net.liftweb.json.JsonAST._
import net.liftweb.json._

trait ApiCalls {

  implicit val formats = DefaultFormats // Brings in default date formats etc.
  val url = "api.wordnik.com"
  val wordResource = "word.json"
  val wordListResource = "wordList.json"
  val accountResource = "account.json"
  val http = new Http with thread.Safety with NoLogging
  val auth_token = getAuthToken

  def getWordDefinition(cmd: DefineCommand) = {
    val req = :/(url) / "v4" / wordResource / cmd.word.word / "definitions" <<? Map("includeRelated" -> "false",
      "includeTags" -> "false",
      "limit" -> cmd.limit.toString,
      "partOfSpeech" -> "noun",
      "useCanonical" -> "false",
      "api_key" -> "a704962c325153433b90203fb980d048cf51d3272103ad71a")
    val definitions = parseJson(req).extract[List[WordResource]]
    definitions.map(_.text.getOrElse(""))
  }

  def getListedWords(cmd: ListedWordsCommand) = {
    val req = :/(url) / "v4" / wordListResource / cmd.wl.permalink / "words" <<? Map("includeRelated" -> "false",
      "sortBy" -> "createDate",
      "sortOrder" -> "desc",
      "api_key" -> "a704962c325153433b90203fb980d048cf51d3272103ad71a",
      "limit" -> cmd.limit.toString) <:< Map(
      "auth_token" -> auth_token)
    val words = parseJson(req).extract[List[WordResource]]
    words.map(_.word)
  }

  def parseJson(req: Request) = http(req >- { resp => parse(resp) })

  def getAuthToken = {
    val req = :/(url) / "v4" / accountResource / "authenticate" / "Janx" <<? Map("password" -> "wordnikpass", "api_key" -> "a704962c325153433b90203fb980d048cf51d3272103ad71a")
    val token = parseJson(req).extract[AuthTokenResource]
    token.token
  }
}
