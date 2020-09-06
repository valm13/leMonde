package yncrea.isen.cir3.ctrl

import yncrea.isen.cir3.model.IMyArticlesModel
import yncrea.isen.cir3.view.IMyArticlesView

class MyArticleDefaultController(var model : IMyArticlesModel) {
    private var views: ArrayList<IMyArticlesView>

    init {
        this.views = ArrayList()
    }

    fun registerView(v: IMyArticlesView){
        this.views.add(v)
        this.model.register(v)
    }

    fun displayView(){
        views.forEach{
            it.display()
        }
    }

    fun closeView(){
        views.forEach {
            it.close()
        }
    }

    fun findArticles(page:Int){
        this.model.findArticlesForPage(page)
    }

    fun findHeadlines(){
        this.model.findHeadlines()
    }
}