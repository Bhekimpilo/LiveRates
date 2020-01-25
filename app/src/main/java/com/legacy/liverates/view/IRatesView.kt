package com.legacy.liverates.view

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Bkay on 18.01.2020.
 */

interface IRatesView {

    fun createList(adapter: RecyclerView.Adapter<*>)
    fun showError(errorString: String)
}
