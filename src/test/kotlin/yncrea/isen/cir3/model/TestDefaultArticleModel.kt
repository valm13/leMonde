package yncrea.isen.cir3.model

import org.apache.logging.log4j.kotlin.Logging
import org.junit.Test
import yncrea.isen.cir3.model.data.LeMondeData
import yncrea.isen.cir3.model.impl.MyDefaultArticleModel
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestDefaultArticleModel {
    companion object : Logging {
        val TOTAL_RESULTS:Int = 3415
        val NUMBER_ARTICLES:Int = 20
        val NUMBER_ARTICLES_HEADLINES:Int = 10
    }

    @Test
    fun testObserverForArticles() {
        var passObserver: Boolean = false
        val model:MyDefaultArticleModel = MyDefaultArticleModel()

        val myObserver = object : IMyArticlesModelObservable{
            override fun updateArticlesHeadlines(data: Any) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun updateArticles(data:Any){
                passObserver = true
                logger.info("updateArticles with : $data")
                assertEquals(LeMondeData::class.java, data::class.java)
            }
        }

        model.register(myObserver)

        model.articles = LeMondeData()
        assertTrue(passObserver)
    }

    @Test
    fun testFindArticlesFor(){
        var passObserver: Boolean = false
        val model:MyDefaultArticleModel = MyDefaultArticleModel()

        val myObserver = object : IMyArticlesModelObservable{
            override fun updateArticles(data:Any){
                passObserver = true
                logger.info("updateArticles with : $data")
                assertEquals(LeMondeData::class.java, data::class.java)
            }

            override fun updateArticlesHeadlines(data: Any) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        model.register(myObserver)

        model.findArticlesForPage(1)
        logger.info("wait data ...")
        Thread.sleep(10000)

        assertTrue(passObserver)
        assertEquals(TOTAL_RESULTS,model.articles.totalResults)
        assertEquals(NUMBER_ARTICLES,model.articles.articles.size)
    }

    @Test
    fun testFindHeadlines(){
        var passObserver: Boolean = false
        val model:MyDefaultArticleModel = MyDefaultArticleModel()

        val myObserver = object : IMyArticlesModelObservable{
            override fun updateArticlesHeadlines(data: Any) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun updateArticles(data:Any){
                passObserver = true
                logger.info("updateArticles with : $data")
                assertEquals(LeMondeData::class.java, data::class.java)
            }
        }

        model.register(myObserver)

        model.findHeadlines()
        logger.info("wait data ...")
        Thread.sleep(10000)

        assertTrue(passObserver)
        assertEquals(NUMBER_ARTICLES_HEADLINES,model.articles.articles.size)
    }
}