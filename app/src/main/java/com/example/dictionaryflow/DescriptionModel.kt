package com.example.dictionaryflow

object DescriptionModel {

    data class Describe(val keyValue: Any) {

        fun getTitle(): String {
            return ::keyValue.name
        }

        fun getDescription(): String {
            return "Description for ${::keyValue.name}"
        }

    }

}