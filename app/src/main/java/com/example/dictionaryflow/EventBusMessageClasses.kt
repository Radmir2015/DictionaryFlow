package com.example.dictionaryflow

class MessageEvent(val message: String)
class WordAndObjectEvent(val word: String?, val obj: Model.Result?)
class UpdateDatabase(val word: String?)