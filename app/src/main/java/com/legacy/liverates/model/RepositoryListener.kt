package com.legacy.liverates.model

interface RepositoryListener {
    fun ratesFromAPI(data: Map<String, Float>)
    fun errorFromRepository(errorString: String)
}
