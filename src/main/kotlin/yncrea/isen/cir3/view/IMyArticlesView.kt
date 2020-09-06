package yncrea.isen.cir3.view

import yncrea.isen.cir3.model.IMyArticlesModelObservable

interface IMyArticlesView :IMyArticlesModelObservable{
    abstract fun display()
    abstract fun close()
}