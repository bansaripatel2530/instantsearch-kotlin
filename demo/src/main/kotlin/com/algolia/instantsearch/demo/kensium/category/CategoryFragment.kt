package com.algolia.instantsearch.demo.kensium.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import kotlinx.android.synthetic.main.kensium_category.*


class CategoryFragment : Fragment() {

    private val adapter = CategoryAdapter().apply {
        onClick = { facet ->
            val bundle =  bundleOf("categoryLvl0" to facet.value)

            findNavController().navigate(R.id.navigateToFragmentProduct, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shared = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)

        shared.searcherForFacets.let {
            it.response?.let { response -> adapter.submitList(response.facets) }
            it.onResponseChanged += { response -> adapter.submitList(response.facets) }
            it.onErrorChanged += { error -> error.printStackTrace() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryList.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }
}