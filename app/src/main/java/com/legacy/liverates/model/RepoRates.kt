package com.legacy.liverates.model

interface RepoRates {

    val nameAndValue: Map<String, Float>?
    fun loopRequest()
    fun stopRequest()

    fun setListener(listener: RepositoryListener)
    fun removeListener()
}
