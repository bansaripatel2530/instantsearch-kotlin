package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.search.helper.deserialize
import com.algolia.search.model.ObjectID
import kotlinx.android.synthetic.main.include_search.*
import java.lang.Exception


class KensiumActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kensium_activity)
        val viewModel = ViewModelProviders.of(this).get(KensiumViewModel::class.java)
        viewModel.configProduct.observe(this, Observer {
           try{
               if(it){
                   viewModel.configProduct.postValue(false)
               }
           }catch (ex:Exception){

           }

        })
        viewModel.product.observe(this, Observer { hits -> viewModel.adapterProduct.submitList(hits) })

        viewModel.searcher.onResponseChanged += {
            viewModel.product
                    .value?.dataSource?.invalidate()
        }
        val searchBoxViewModel = SearchBoxViewModel()
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        searchBoxViewModel.connectView(searchBoxView)
        searchBoxViewModel.connectSearcher(viewModel.searcher)
        searchBoxViewModel.onQueryChanged += {
            viewModel.product
                    .value?.dataSource?.invalidate()
        }

    }
}