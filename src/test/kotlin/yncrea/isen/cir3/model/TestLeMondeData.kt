package yncrea.isen.cir3.model

import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import org.junit.Test
import yncrea.isen.cir3.model.data.LeMondeData
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestLeMondeData {
    @Test
    fun articlesPage1(){
        val (request, response, result) = "https://newsapi.org/v2/everything?domains=lemonde.fr&apiKey=9cc2e521ef2348e3a6db1f290e25a37e&page=1".httpGet().responseObject(LeMondeData.Deserializer())

        assertTrue(response.isSuccessful)
        result.component1().also { it }
        result.component1().also {
            assertNotNull(it).also { println(it) }
        } ?.let {
            assertEquals("ok",it.status)
            assertEquals(20,it.articles.size)
        }
    }

    @Test
    fun articlesHeadlines(){
        val (request, response, result) = "https://newsapi.org/v2/top-headlines?sources=le-monde&apiKey=9cc2e521ef2348e3a6db1f290e25a37e".httpGet().responseObject(LeMondeData.Deserializer())

        assertTrue(response.isSuccessful)
        result.component1().also { it }
        result.component1().also {
            assertNotNull(it).also { println(it) }
        } ?.let {
            assertEquals(10,it.articles.size)
        }
    }
}