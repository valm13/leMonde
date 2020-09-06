package yncrea.isen.cir3.model.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class LeMondeData (
    val status : String,
    val totalResults : Int,
    val articles : List<ArticleData>
){
    constructor():this("",0,emptyList())

    class Deserializer : ResponseDeserializable<LeMondeData> {
        override fun deserialize(content : String): LeMondeData
                = Gson().fromJson(content, LeMondeData::class.java)
    }
}