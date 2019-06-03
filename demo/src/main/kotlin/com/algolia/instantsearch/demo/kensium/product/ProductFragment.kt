package com.algolia.instantsearch.demo.kensium.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.configureRecyclerView
import com.algolia.instantsearch.demo.configureSearcher
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumActivity
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.demo.list.Movie
import com.algolia.instantsearch.helper.android.selectable.SelectableSegmentViewSpinner
import com.algolia.instantsearch.helper.index.connectView
import com.algolia.search.helper.deserialize
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.kensium_product.*


class ProductFragment : Fragment() {

    lateinit var viewModel: KensiumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)
        if(arguments != null){
            val category = arguments!!.getString("categoryLvl0")!!
            val filter = Filter.Facet(Kensium.categoryLvl0, category)

            viewModel.searcher.let {
                /**
                 * When we enter the screen, first we clear the FilterState.
                 * Then we add the "categoryLvl0" that was passed as an argument.
                 */
                it.filterState.notify {
                    clear()
                    add(Kensium.groupIDCategoryLvl0, filter)
                }
            }
        }

//        viewModel.searcher.onResponseChanged += {
//            viewModel.product.value?.dataSource?.invalidate()
//        }



        viewModel.product.observe(this, Observer { hits -> viewModel.adapterProduct.submitList(hits) })


        viewModel.indexSegmentViewModel.onSelectedComputed += {
            //            Log.e("tag","onSelectedComputed")
            viewModel.product.value?.dataSource?.invalidate()

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterSortBy = ArrayAdapter<String>(requireContext(), R.layout.menu_item)
        val sortByView = SelectableSegmentViewSpinner(spinner, adapterSortBy)

        viewModel.indexSegmentViewModel.connectView(sortByView, viewModel.presenterSortBy)

        productList.adapter = viewModel.adapterProduct

        (activity as KensiumActivity).configureRecyclerView(productList, viewModel.adapterProduct)
        button.setOnClickListener { findNavController().navigate(R.id.navigateToFragmentFilter) }
    }
}