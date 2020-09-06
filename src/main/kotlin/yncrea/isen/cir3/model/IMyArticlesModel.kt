package yncrea.isen.cir3.model

interface IMyArticlesModel{
    abstract fun register(listener:IMyArticlesModelObservable)
    abstract fun unregister(listener:IMyArticlesModelObservable)

    // API
    public fun findArticlesForPage(page:Int)
    public fun findHeadlines()
}