package com.wordnik.api.dsl

case class WordResource(word: String, text: Option[String])
case class AuthTokenResource(token: String, userId: String, userSignature: String)
