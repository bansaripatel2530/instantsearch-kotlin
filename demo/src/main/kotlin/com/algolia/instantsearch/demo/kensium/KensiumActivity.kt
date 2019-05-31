package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.search.helper.deserialize
import com.algolia.search.model.ObjectID


class KensiumActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        val viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)
        viewModel.configProduct.observe(this, Observer {
            if(it){
                viewModel.searcher.onResponseChanged += { response ->
                        viewModel.product.observe(this, Observer { hits -> viewModel.adapterProduct.submitList(hits) })

                }


            }
        })

    }
}