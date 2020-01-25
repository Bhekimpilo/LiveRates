package com.legacy.liverates.viewmodel

import android.content.Context
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

import com.jakewharton.rxbinding2.widget.RxTextView

import java.util.HashMap
import java.util.Locale

import butterknife.ButterKnife
import com.blongho.country_data.World
import com.legacy.liverates.R
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_row.view.*
import java.lang.Float.valueOf


class RatesAdapter (val viewModel: RatesViewModel) : RecyclerView.Adapter<RatesAdapter.CurrencyItemViewHolder>() {
    private val editTextCache = HashMap<String, EditText>()

    override fun getItemCount(): Int {
        return viewModel.modelList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.view_row, parent, false)
        return CurrencyItemViewHolder(rowView)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {

        val item = viewModel.modelList[position]
        bindToViewHolder(holder, item)

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun bindToViewHolder(holder: CurrencyItemViewHolder, item: Currency) {
        populateWithModel(holder, item)
        registerClickAndFocus(holder, item)
        editTextCache[item.name] = holder.currencyValue!!

    }

    private fun registerClickAndFocus(holder: CurrencyItemViewHolder, item: Currency) {
        //Set action for click on whole view
        holder.itemView.setOnClickListener { moveToTop(item, holder) }
        //Set action for in focus for EditText
        holder.currencyValue!!.onFocusChangeListener = View.OnFocusChangeListener { _, isInFocus -> handleFocusForItem(isInFocus, item, holder) }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun populateWithModel(holder: CurrencyItemViewHolder, item: Currency) {
        //Fill view with data

        val code = item.name
        holder.currencyCode!!.setText(code)

        val currencyDisplayName = java.util.Currency.getInstance(code).displayName
        holder.currencyName?.setText(currencyDisplayName)

        Picasso.get().load(World.getFlagOf(code.substring(0,2))).into(holder.imgFlag)
        setValueToEditText(holder.currencyValue!!, item.value!!)
    }

    private fun handleFocusForItem(isInFocus: Boolean, item: Currency, holder: CurrencyItemViewHolder) {
        if (isInFocus) {
            moveToTop(item, holder)
            holder.setReactiveEditText(TextChangeEventListener(item, holder))
            setCursorPosition(holder)
        } else {
            destroyEvent(holder)
        }
    }

    private fun destroyEvent(holder: CurrencyItemViewHolder) {
        //Dispose
        holder.disposeReactiveEditText()
        //Remove soft keyboardd
        val imm = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(holder.currencyValue!!.windowToken, 0)
    }

    private fun setCursorPosition(holder: CurrencyItemViewHolder) {
        val posOfBeforePoint = holder.currencyValue!!.text.toString().lastIndexOf(".")
        holder.currencyValue!!.setSelection(posOfBeforePoint)
    }

    private fun TextChangeEventListener(item: Currency, holder: CurrencyItemViewHolder): Disposable {
        return RxTextView.textChanges(holder.currencyValue!!)
                .skip(1)
                .map { text -> helperMapEmptyStringToZero(text.toString()) }
                .doOnNext { value ->
                    viewModel.modifyCurrentAmountWithItem(valueOf(value), item.value!!)
                    updateAllCurrencyValues()
                }
                .subscribe()
    }

    private fun helperMapEmptyStringToZero(str: String?): String {
        return if (str == null || str.isEmpty() || str == ".")
            "0"
        else
            str
    }

    private fun setValueToEditText(editText: EditText, rate: Float) {
        val result = rate * viewModel.currentAmount!!
        editText.setText(String.format(Locale.US, "%.2f", result))
    }


    private fun moveToTop(item: Currency, holder: CurrencyItemViewHolder) {
        //Position before move
        val pos = viewModel.modelList.indexOf(item)


        if (viewModel.modelList.remove(item)) {
            viewModel.modelList.add(0, item)

            //set focus to top element
            holder.currencyValue!!.requestFocus()


            // Show soft keyboard for the user to enter the value.
            val keybaordManager = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keybaordManager.showSoftInput(holder.currencyValue, InputMethodManager.SHOW_IMPLICIT)

            //let recyclerView refresh and animate it
            notifyItemMoved(pos, 0)
        }
    }

    fun updateAllCurrencyValues() {
        for (currencyName in editTextCache.keys) {
            val currencyCode = getCurrencyCode(currencyName)
            updateCurrencyValue(currencyName, currencyCode)
        }
    }

    private fun updateCurrencyValue(currencyName: String, value: Float?) {
        val editText = editTextCache[currencyName]

        var isTopItem = false
        if (viewModel.modelList.size > 0) {
            val topItem = viewModel.modelList[0]
            if (topItem.name == currencyName)
                isTopItem = true
        }

        if (!isTopItem)
            setValueToEditText(editText!!, value!!)
    }

    private fun getCurrencyCode(name: String): Float? {
        for (item in viewModel.modelList) {
            if (item.name == name)
                return item.value
        }
        return java.lang.Float.NaN
    }


    inner class CurrencyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currencyCode: TextView? = null
        var currencyValue: EditText? = null
        var currencyName: TextView? = null
        var imgFlag: ImageView? = null

        private var reactiveEditText: Disposable? = null

        init {
            ButterKnife.bind(this, itemView)
            currencyCode = itemView.countryText
            currencyName = itemView.currencyText
            imgFlag = itemView.flagView
            currencyValue = itemView.valueText
        }

        fun disposeReactiveEditText() {
            if (reactiveEditText != null)
                reactiveEditText!!.dispose()
        }

        fun setReactiveEditText(reactiveEditText: Disposable) {
            this.reactiveEditText = reactiveEditText
        }
    }

    companion object {

        fun create(viewModel: RatesViewModel): RatesAdapter {
            return RatesAdapter(viewModel)
        }
    }


}