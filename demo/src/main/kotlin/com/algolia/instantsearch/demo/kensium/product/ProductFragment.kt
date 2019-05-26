package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.search.helper.deserialize
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    private val adapter = ProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = arguments!!.getString("categoryLvl0")!!
        val shared = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)
        val filter = Filter.Facet(Kensium.categoryLvl0, category)

        shared.searcher.let {
            /**
             * When we enter the screen, first we clear the FilterState.
             * Then we add the "categoryLvl0" that was passed as an argument.
             */
            it.filterState.notify {
                clear()
                add(Kensium.groupIDCategoryLvl0, filter)
            }
            it.onResponseChanged += { response ->
                adapter.submitList(response.hits.deserialize(Product.serializer()))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}