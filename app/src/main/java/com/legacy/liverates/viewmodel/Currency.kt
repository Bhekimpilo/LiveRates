package com.legacy.liverates.viewmodel

class Currency private constructor(val name: String, var value: Float?) {
    companion object {

        fun create(name: String, value: Float?): Currency {
            return Currency(name, value)
        }
    }
}
