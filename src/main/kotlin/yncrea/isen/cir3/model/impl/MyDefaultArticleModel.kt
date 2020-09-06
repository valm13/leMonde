package yncrea.isen.cir3.model.impl

import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.Logging
import yncrea.isen.cir3.model.IMyArticlesModel
import yncrea.isen.cir3.model.IMyArticlesModelObservable
import yncrea.isen.cir3.model.data.LeMondeData
import kotlin.properties.Delegates

class MyDefaultArticleModel :IMyArticlesModel{
    companion object : Logging

    private var listeners: ArrayList<IMyArticlesModelObservable>

    init {
        listeners = ArrayList()
    }

    public var articles : LeMondeData by Delegates.observable(LeMondeData()) { property, oldValue, newValue ->
        logger.info("Articles property change, notify observer")
        for (listener in listeners) {
            listener.updateArticles(newValue)
        }
    }

    public var articlesHeadlines : LeMondeData by Delegates.observable(LeMondeData()) { property, oldValue, newValue ->
        logger.info("ArticlesHeadlines property change, notify observer")
        for (listener in listeners) {
            listener.updateArticlesHeadlines(newValue)
        }
    }

    override fun register(listener: IMyArticlesModelObservable) {
        listeners.add(listener)
    }

    override fun unregister(listener: IMyArticlesModelObservable) {
        listeners.remove(listener)
    }

    private suspend fun getArticlesForPage(page:Int){
        "https://newsapi.org/v2/everything?domains=lemonde.fr&apiKey=9cc2e521ef2348e3a6db1f290e25a37e&page=$page".httpGet().responseObject(LeMondeData.Deserializer()){
            request, response, result ->
            logger.info("StatusCode : ${response.statusCode}")
            result.component1()?.let {
                articles = it
            }
        }
    }

    public override fun findArticlesForPage(page:Int){
        logger.info("get articles for page $page")
        GlobalScope.launch { getArticlesForPage(page) }
    }

    private suspend fun getHeadlines(){
        "https://newsapi.org/v2/top-headlines?sources=le-monde&apiKey=9cc2e521ef2348e3a6db1f290e25a37e".httpGet().responseObject(LeMondeData.Deserializer()){
            request, response, result ->
            logger.info("StatusCode : ${response.statusCode}")
            result.component1()?.let {
                articlesHeadlines = it
            }
        }
    }

    override fun findHeadlines() {
        logger.info("get headlines")
        GlobalScope.launch { getHeadlines() }
    }
}