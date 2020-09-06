package yncrea.isen.cir3.model.data

import java.net.URL
import java.util.*

data class ArticleData(
        val author : String,
        val title : String,
        val description : String,
        val url : String,
        val urlToImage : String,
        val publishedAt : Date,
        val content : String
) {
    constructor():this("","","","","",Date(),"")

}