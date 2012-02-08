package com.wordnik.api.dsl

case class DefineCommand(w: Word)

case class ListCommand(wl: WordList)

case class Word(w: String)

case class WordList(permalink: String)
