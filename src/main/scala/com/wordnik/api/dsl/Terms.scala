package com.wordnik.api.dsl

case class DefineCommand(word: Word, limit: Int=1)

case class ListedWordsCommand(wl: WordList)

case class Word(word: String)

case class WordList(permalink: String)
