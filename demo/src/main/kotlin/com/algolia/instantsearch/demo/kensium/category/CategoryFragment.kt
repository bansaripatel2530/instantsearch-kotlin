package com.algolia.instantsearch.demo.kensium.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.searchbox.connectView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectSearcher
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.include_search.searchView
import kotlinx.android.synthetic.main.kensium_activity.*
import kotlinx.android.synthetic.main.kensium_category.*


class CategoryFragment : Fragment() {

    var viewModel: KensiumViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as KensiumActivity).viewModel
        viewModel?.adapterCategoryLvl0?.apply {
            onClick = { facet ->
                val  bundle =  bundleOf("categoryLvl0" to facet.value)

                findNavController().navigate(R.id.navigateToFragmentProduct, bundle)
            }
        }



//        activity?.searchView?.setOnQueryTextListener(object :SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if(isVisible && !newText.isNullOrEmpty()){
//                    val bundle =  bundleOf("query" to newText.toString())
//                    findNavController().navigate(R.id.navigateToFragmentProduct,bundle)
//                }
//                return false
//
//            }
//
//        })

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = viewModel?.adapterCategoryLvl0
        }
    }
}