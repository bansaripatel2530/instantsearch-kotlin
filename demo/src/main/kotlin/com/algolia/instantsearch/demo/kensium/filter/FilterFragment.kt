package com.algolia.instantsearch.demo.kensium.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.filter.facet.FacetListAdapter
import com.algolia.instantsearch.demo.kensium.Kensium
import com.algolia.instantsearch.demo.kensium.KensiumViewModel
import com.algolia.instantsearch.helper.attribute.AttributeMatchAndReplace
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.kensium_filter.*


class FilterFragment : Fragment() {

    private val filterPresenter = FilterPresenterImpl(AttributeMatchAndReplace(Kensium.price, "price"))
    private val adapterBrand = FacetListAdapter()
    private val adapterCategory = FacetListAdapter()
    private val adapterPrice = FilterPriceAdapter(filterPresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shared = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)

        val brandViewModel = FacetListViewModel()
        /**
         * You can pass several [FacetSortCriterion] in the [FacetListPresenter.sortBy] parameter.
         * Each of them will be applied. The order in which each of them is passed determines the priority of each sorting criteria.
         */
        val presenter = FacetListPresenter(sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.AlphabeticalDescending))
        val searcher = shared.searcher
        val filterState = searcher.filterState

        brandViewModel.connectFilterState(Kensium.brand, filterState, Kensium.groupIDBrand)
        brandViewModel.connectSearcher(Kensium.brand, searcher)
        brandViewModel.connectView(adapterBrand, presenter)

        val categoryViewModel = FacetListViewModel()

        categoryViewModel.connectFilterState(Kensium.categoryLvl1, filterState, Kensium.groupIDCategoryLvl1)
        categoryViewModel.connectSearcher(Kensium.categoryLvl1, searcher)
        categoryViewModel.connectView(adapterCategory, presenter)

        val priceViewModel = FilterListViewModel.Numeric()
        /**
         * Add the price facet to access FacetStats later.
         */
        searcher.query.addFacet(Kensium.price)
        priceViewModel.connectFilterState(filterState, Kensium.groupIDPrice)
        priceViewModel.connectView(adapterPrice)

        searcher.search()
        searcher.onResponseChanged += { response ->
            /**
             * Here, use the facetStats to do dynamic computation of the price filters.
             */
            val facetStatsPrice = response.facetStats[Kensium.price]

            /**
             * Example of a price filters. Use "facetStatsPrice" above to compute proper filter ranges.
             */
            priceViewModel.items = listOf(
                Filter.Numeric(Kensium.price, NumericOperator.Less, 10),
                Filter.Numeric(Kensium.price, 10..20),
                Filter.Numeric(Kensium.price, 20..30),
                Filter.Numeric(Kensium.price, NumericOperator.Greater, 30)
            )
        }
        searcher.onErrorChanged += { error -> error.printStackTrace() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.kensium_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shared = ViewModelProviders.of(requireActivity()).get(KensiumViewModel::class.java)

        brandFilterList.let {
            it.adapter = adapterBrand
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        categoryFilterList.let {
            it.adapter = adapterCategory
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        priceFilterList.let {
            it.adapter = adapterPrice
            it.layoutManager = LinearLayoutManager(context)
            it.itemAnimator = null
        }
        buttonClear.setOnClickListener {
            /**
             * This will clear only price, brand and categoryLvl0.
             * It will not clear categoryLvl0.
             */
            shared.searcher.filterState.notify {
                clear(Kensium.groupIDCategoryLvl1)
                clear(Kensium.groupIDPrice)
                clear(Kensium.groupIDBrand)
            }
        }
        buttonSubmit.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}