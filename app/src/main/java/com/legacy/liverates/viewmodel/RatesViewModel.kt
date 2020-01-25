package com.legacy.liverates.viewmodel

import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import com.legacy.liverates.Config
import com.legacy.liverates.model.RepoRates
import com.legacy.liverates.model.RepositoryListener
import com.legacy.liverates.view.IRatesView
import java.util.Collections.emptyList

class RatesViewModel(private val repository: RepoRates) : RepositoryListener {
    private var view: IRatesView? = null

    private var ratesAdapter: RatesAdapter? = null
    var modelList = emptyList<Currency>()
        private set
    var currentAmount: Float? = Config.BASE_AMOUNT
        private set

    fun attachView(view: IRatesView) {
        this.view = view
        repository.setListener(this)
        repository.loopRequest()
        //createListModelIfNeeded(repository.nameAndValue!!)

    }

    private fun createListModelIfNeeded(ratesFromCache: Map<String, Float>) {
        if (modelList.size == 0) {
            modelList = mapRepoDataToModel(ratesFromCache)
            view!!.createList(buildAdapter())
        }
    }

    fun detachView() {
        ratesAdapter = null
        repository.stopRequest()
        repository.removeListener()
        this.view = null
    }

    private fun buildAdapter(): RecyclerView.Adapter<*> {
        if (ratesAdapter == null)
            ratesAdapter = RatesAdapter.create(this)
        return ratesAdapter as RatesAdapter
    }

    private fun mapRepoDataToModel(repoData: Map<String, Float>?): List<Currency> {
        val returnValue = ArrayList<Currency>()
        if (repoData != null) {
            for (currencyName in repoData.keys) {
                val value = repoData[currencyName]
                if (currencyName == Config.BASE_CURRENCY)
                    returnValue.add(0, Currency.create(currencyName, value))
                else
                    returnValue.add(Currency.create(currencyName, value))
            }
        }
        return returnValue
    }

    override fun ratesFromAPI(data: Map<String, Float>) {
        if (modelList.size == 0)
            createListModelIfNeeded(data)
        else {
            updateListModelWithNewData(data)
            if (ratesAdapter != null)
                ratesAdapter!!.updateAllCurrencyValues()
            else
                view!!.createList(buildAdapter())
        }
    }

    private fun updateListModelWithNewData(data: Map<String, Float>) {
        for (currencyName in data.keys) {
            val value = data[currencyName]

            for (item in modelList) {
                if (item.name == currencyName) {
                    item.value = value
                    break
                }
            }
        }
    }

    override fun errorFromRepository(errorString: String) {
        view!!.showError(errorString)
    }

    fun modifyCurrentAmountWithItem(newValue: Float, koef: Float) {
        if (koef > 0.0f)
            this.currentAmount = newValue / koef
    }

}
