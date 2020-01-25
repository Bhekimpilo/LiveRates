package com.legacy.liverates.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

import javax.inject.Inject

import butterknife.BindView
import butterknife.ButterKnife
import com.legacy.liverates.R
import com.legacy.liverates.App
import com.legacy.liverates.network.InternetAccess
import com.legacy.liverates.viewmodel.RatesViewModel
import kotlinx.android.synthetic.main.activity_currencies.*



class RatesActivity : AppCompatActivity(), IRatesView {

    @Inject
    var viewModel: RatesViewModel? = null

    @BindView(R.id.rates_list)
    internal var ratesList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)
        ButterKnife.bind(this)
        App.component?.inject(this)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {

        super.onStart()
        if (InternetAccess().isAvailable(this))
            viewModel!!.attachView(this)

        else {
            val alertDialog = AlertDialog.Builder(this)
                    .setTitle(R.string.errorTitle)
                    .setMessage(R.string.errorMessage)
                    .setNegativeButton(R.string.exit) { _, _ ->
                        System.exit(0)
                    }
                    .setIcon(getDrawable(android.R.drawable.stat_sys_warning))
                    .setPositiveButton(R.string.retry) { _, _ ->
                        onStart()
            }

            alertDialog.show()
        }

        pBar.visibility = View.INVISIBLE
    }

    override fun createList(adapter: RecyclerView.Adapter<*>) {
        rates_list.layoutManager = LinearLayoutManager(this)
        rates_list.setHasFixedSize(true)
        rates_list.setItemViewCacheSize(adapter.itemCount)
        rates_list.adapter = adapter
    }

    override fun showError(errorString: String) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        ratesList!!.adapter = null
        viewModel!!.detachView()
        super.onStop()
    }

}
