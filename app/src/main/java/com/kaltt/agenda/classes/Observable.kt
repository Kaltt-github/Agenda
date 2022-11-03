package com.kaltt.agenda.classes

import java.util.function.Function
import kotlin.collections.ArrayList

// N is the observable class
// M is the observer class
// B is the return type of notifying the observers
interface Observable<N,M,B> {
    var observable: N
    var notifiers: ArrayList<Function<M, B>>
    var observers: ArrayList<M>

    fun addObserver(observer: M, notifier: Function<M, B>){
        this.observers.add(observer)
        this.notifiers.add(notifier)
    }

    fun notifyObservers(): ArrayList<B> {
        val result = ArrayList<B>()
        for ((i, notifier) in notifiers.withIndex()) {
            result.add(notifier.apply(observers[i]))
        }
        return result
    }
}