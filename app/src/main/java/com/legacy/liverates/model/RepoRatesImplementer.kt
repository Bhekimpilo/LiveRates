package com.legacy.liverates.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log

import com.google.gson.Gson
import com.legacy.liverates.network.RetrofitCall

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.lang.String.valueOf

class RepoRatesImplementer(context: Context, private val retrofitCall: RetrofitCall) : RepoRates {

    private var currencies: API_Response? = null
    private var prefManager: SharedPrefManager
    private var listener: RepositoryListener? = null

    override val nameAndValue: Map<String, Float>?
        get() {
            val cachedData = prefManager.load()
            currencies = Gson().fromJson<API_Response>(cachedData, API_Response::class.java)
            if (currencies != null && currencies!!.rates != null) {
                defineBaseCurrency()
                return currencies?.rates!!
            }

             return null
        }


    init {
        this.prefManager = SharedPrefManager(context)
    }

    fun defineBaseCurrency() {
        currencies?.base = valueOf(1.0f)
    }

    override fun loopRequest() {

        retrofitCall.loopRequest(object : RetrofitCall.NetworkListener {
            @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
            override fun onSuccess(newRates: API_Response) {
                currencies = newRates
                if (listener != null)
                    listener!!.ratesFromAPI(proceedToListener(currencies!!)!!)
                startSavingData(newRates)
            }

            override fun onFailure(t: Throwable) {
                Log.e("failure", t.localizedMessage!!)
                val errorString = t.localizedMessage
                if (listener != null)
                    listener!!.errorFromRepository(errorString!!)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun startSavingData(newCurrencies: API_Response) {
        val stringData = Gson().toJson(newCurrencies)
        Flowable.just(stringData)
                .subscribeOn(Schedulers.io())
                .doOnNext { str -> prefManager.save(str) }
                .subscribe()
    }

    override fun stopRequest() {
        retrofitCall.stopRequest()
    }

    override fun setListener(listener: RepositoryListener) {
        this.listener = listener
    }

    override fun removeListener() {
        this.listener = null
    }

    private fun proceedToListener(currencies: API_Response): Map<String, Float>? {
        this.currencies = currencies
        defineBaseCurrency()
        return currencies.rates
    }
}
