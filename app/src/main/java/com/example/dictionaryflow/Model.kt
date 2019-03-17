package com.example.dictionaryflow

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object Model {
    data class Result(
        val results: ArrayList<Results>?,
        val syllables: Syllables?,
        var mpronunciation: String?,
        val word: String?,
        val frequency: Number
    ) {
        fun setPronunciationValues(pronunciationValues: Any) {
            mpronunciation = when (pronunciationValues) {
                is String -> pronunciationValues //Pronunciation(pronunciationValues)
                is Pronunciation -> pronunciationValues.all
                else -> null
            }
        }

        class ResultsDeserializer : JsonDeserializer<Model.Result> {

            @Throws(JsonParseException::class)
            override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Model.Result {
                val resultJSON: Model.Result = Gson().fromJson<Model.Result>(json, Model.Result::class.java)
                val jsonObject = json.asJsonObject

                if (jsonObject.has("pronunciation")) {
                    val elem = jsonObject.get("pronunciation")
                    if (elem != null && !elem.isJsonNull) {
                        val values: Any =
                            if (!elem.isJsonObject)
                                elem.asString
                            else
                                Gson().fromJson<Pronunciation>(
                                    elem,
                                    object : TypeToken<Pronunciation>() {
                                }.type)
                        resultJSON.setPronunciationValues(values)
                    }
                }
                return resultJSON
            }
        }

    }

    data class Results(
        val definition: String,
        val partOfSpeech: String,
        val synonyms: ArrayList<String>?,
        val antonyms: ArrayList<String>?,
        val also: ArrayList<String>?,
        val attribute: ArrayList<String>?,
        val regionOf: ArrayList<String>?,
        val instanceOf: ArrayList<String>?,
        val inCategory: ArrayList<String>?,
        val hasParts: ArrayList<String>?,
        val hasMembers: ArrayList<String>?,
        val hasInstances: ArrayList<String>?,
        val hasTypes: ArrayList<String>?,
        val memberOf: ArrayList<String>?,
        val partOf: ArrayList<String>?,
        val typeOf: ArrayList<String>?,
        val derivation: ArrayList<String>?,
        val similarTo: ArrayList<String>?,
        val examples: ArrayList<String>?,
        val usageOf: ArrayList<String>?
    )

    data class Syllables (
        val count: Number,
        val list: ArrayList<String>?
    )

    data class Pronunciation (
        val all: String
    )
}