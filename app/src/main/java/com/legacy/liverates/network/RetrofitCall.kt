package com.legacy.liverates.network

import android.util.Log
import com.legacy.liverates.Config.BASE_CURRENCY
import com.legacy.liverates.Config.BASE_URL
import com.legacy.liverates.model.API_Response


import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCall {

    private val retrofitService: IRetrofitService
    private var networkObservable: Disposable? = null


    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        retrofitService = retrofit.create(IRetrofitService::class.java)
    }

    //Create an Observable to call the API at a preset interval and update the model
    fun loopRequest(@NonNull callback: NetworkListener) {
        networkObservable = Observable.interval(1, TimeUnit.SECONDS)
                .flatMap<API_Response> { _ ->
                    retrofitService.rates(BASE_CURRENCY)
                            .flatMap<API_Response>{ Observable.just(it) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            //Handle the Response and Errors
                            .doOnNext { currencies: API_Response ->
                                Log.d("server", currencies.base + "   " + currencies.date)
                                callback.onSuccess(currencies)
                            }
                            .doOnError { throwable: Throwable ->
                                Log.e("server", "Error: " + throwable.message)
                                callback.onFailure(throwable)
                            }
                }.subscribe()
    }

    fun stopRequest() {
        if (null != networkObservable) {
            networkObservable!!.dispose()
            networkObservable = null
        }
    }

    interface NetworkListener {
        fun onSuccess(currencies: API_Response)
        fun onFailure(t: Throwable)
    }
}
