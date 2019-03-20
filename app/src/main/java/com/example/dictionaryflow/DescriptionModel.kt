package com.example.dictionaryflow

import java.util.*


object DescriptionModel {

    data class Description(val title: String, val description: String?, val value: Any?)

    class CallOrder(private val obj: Model.Result) {

//        fun getOrder(): List<String> {
//            val result = ArrayList<String>()
//            for (prop in Model.Result::class.declaredMemberProperties) {
//                if (prop.name == "results")
//                    for (prop1 in Model.Results::class.declaredMemberProperties)
//                        result.add("${prop1.name} = ${prop1.get(prop.get(this.obj) as Model.Results)}")
//            }
//            return result.subList(0, 1)
//        }

        fun getOrder(): List<Description> {
            return listOf(
                Description( "Pronunciation", "This word pronunciation is ${obj.mpronunciation}", obj.mpronunciation),
                Description( "Syllables", "This word contains ${obj.syllables?.count} syllables, such as ${obj.syllables?.list}", obj.syllables?.list),
                Description( "Frequency", "Frequency of the word: ${obj.frequency}", obj.frequency)
            )
        }

        fun getResultsOrder(): ArrayList<ArrayList<Description>> {
            val result : ArrayList<ArrayList<Description>> = ArrayList()
            obj.results?.forEach {
                result.add(
                    arrayListOf(
                        Description("Definition", "${obj.word?.capitalize()} - ${it.definition}", it.definition),
                        Description("Examples of usage", it.examples?.joinToString(separator = "\n"), it.examples),
                        Description("Part of speech", "This is a/an ${it.partOfSpeech}.", it.partOfSpeech),
                        Description("Synonyms", "The word has such synonyms as ${it.synonyms?.joinToString()}.", it.synonyms),
                        Description("Antonyms", "The word has such antonyms as ${it.antonyms?.joinToString()}.", it.antonyms),
                        Description("Region of", "The word is a region of ${it.regionOf?.joinToString()}.", it.regionOf),
                        Description("Instance of", "The word is an instance of ${it.instanceOf?.joinToString()}.", it.instanceOf),
                        Description("In category", "The word is in a category(ies) of ${it.inCategory?.joinToString()}.", it.inCategory),
                        Description("Has parts", "The word is parts of ${it.hasParts?.joinToString()}.", it.hasParts),
                        Description("Has members", "The word has members such as ${it.hasMembers?.joinToString()}.", it.hasMembers),
                        Description("Has instances", "The word has instances of ${it.hasInstances?.joinToString()}.", it.hasInstances),
                        Description("Has types", "The word has such types as ${it.hasTypes?.joinToString()}.", it.hasTypes),
                        Description("Member of", "The word is a member of ${it.memberOf?.joinToString()}.", it.memberOf),
                        Description("Part of", "The word is a part of ${it.partOf?.joinToString()}.", it.partOf),
                        Description("Type of", "The word is a type of ${it.typeOf?.joinToString()}.", it.typeOf),
                        Description("Derivation", "Derives from ${it.derivation?.joinToString()}.", it.derivation),
                        Description("Similar to", "The word is similar to ${it.similarTo?.joinToString()}.", it.similarTo),
                        Description("Usage of", "The word is a usage of the ${it.usageOf?.joinToString()}.", it.usageOf),
                        Description("Has usages", "The word is a domain that includes examples like ${it.hasUsages?.joinToString()}.", it.hasUsages),
                        Description("Verb group", "The word refers to the groups of ${it.verbGroup?.joinToString()}.", it.verbGroup),
                        Description("Entails", "The word entails ${it.entails?.joinToString()}.", it.entails),
                        Description("Also", "The word is used in the phrase ${it.also?.joinToString()}", it.also),
                        Description("Attribute", "The word has attributes such as ${it.attribute?.joinToString()}", it.attribute)
                    )
                )
            }
            return result
        }

//        fun getOrder3(): List<String> {
//            val result = ArrayList<String>()
//            val temp = getOrder2()
//            temp.next()
//            temp.next()
//            val temp1 = (temp.next() as ArrayList<String>).iterator()
//            if (temp1.hasNext())
//                result.add(DescriptionModel.Describe((temp1.next() as Model.Results).definition).getTitle())
//            if (temp1.hasNext())
//                result.add(temp1.next())
//            return result
//        }

    }

//    class Container(vararg values: String) : Iterator<Any> {
//        private val iter: Iterator<String> = values.iterator()
//
//        override fun hasNext(): Boolean = iter.hasNext()
//
//        override fun next(): Any = iter.next()
//    }

}