package com.example.parksmart

class Event<T>(private val data: T) {

    private var done = false

    fun doneEvent(block: Event<T>.(T) -> Unit) {
        if (!done) {
            done = true
            block(data)
        }
    }
}