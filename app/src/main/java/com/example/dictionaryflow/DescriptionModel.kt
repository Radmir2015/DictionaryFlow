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
                Description( "Frequency", "Frequency of the word: ${obj.frequency}", obj.frequency),
                Description( "Pronunciation", "This word pronunciation is ${obj.mpronunciation}", obj.mpronunciation),
                Description( "Syllables", "This word contains ${obj.syllables?.count} syllables, such as ${obj.syllables?.list}", obj.syllables?.list)
            )
        }

        fun getResultsOrder(): ArrayList<ArrayList<Description>> {
            val result : ArrayList<ArrayList<Description>> = ArrayList()
            obj.results?.forEach {
                run {
                    result.add(
                        arrayListOf(
                            Description("Definition", "${obj.word} - ${it.definition}", it.definition),
                            Description("Examples of usage", it.examples?.joinToString(separator = "\n"), it.examples)
                        )
                    )
                }
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