package com.legacy.liverates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.TreeMap

class API_Response {

    @SerializedName("base")
    @Expose
    var base: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("rates")
    @Expose
    var rates: TreeMap<String, Float>? = null
}
