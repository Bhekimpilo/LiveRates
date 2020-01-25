package com.legacy.liverates.network

import com.legacy.liverates.model.API_Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface IRetrofitService {
    @GET("/latest")
    fun rates(@Query("base") base: String): Observable<API_Response>
}
