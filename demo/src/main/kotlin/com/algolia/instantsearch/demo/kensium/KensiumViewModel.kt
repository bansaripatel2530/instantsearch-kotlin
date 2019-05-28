package com.algolia.instantsearch.demo.kensium

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.demo.filter.facet.FacetListAdapter
import com.algolia.instantsearch.demo.kensium.category.CategoryAdapter
import com.algolia.instantsearch.demo.kensium.filter.FilterPriceAdapter
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductAdapter
import com.algolia.instantsearch.helper.attribute.AttributeMatchAndReplace
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.index.IndexPresenter
import com.algolia.instantsearch.helper.index.IndexSegmentViewModel
import com.algolia.instantsearch.helper.index.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.client.Index
import com.algolia.search.helper.deserialize
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


/**
 * This class will be shared with all fragments from the same activity.
 */
class KensiumViewModel : ViewModel() {

    val searcher = SearcherSingleIndex(Kensium.index)
    val searcherForFacets = SearcherForFacets(Kensium.index, Kensium.categoryLvl0)

    /**
     * This is for formatting purposes: We replace "price.USD.default" by "price" when we display our list of Filter.Numeric.
     */
    val filterNumericPresenter = FilterPresenterImpl(AttributeMatchAndReplace(Kensium.price, "price"))

    val adapterCategoryLvl0 = CategoryAdapter()
    val adapterProduct = ProductAdapter()
    val adapterBrand = FacetListAdapter()
    val adapterCategoryLvl1 = FacetListAdapter()
    val adapterPrice = FilterPriceAdapter(filterNumericPresenter)

    /**
     * Here you can define all the index you want. Each time a new index will be selected, the results will automatically change.
     * This achieve the "SortBy" feature.
     */
    val indexSegmentViewModel = IndexSegmentViewModel(items = mapOf(
        0 to Kensium.index,
        1 to Kensium.indexPriceAsc
    ))

    /**
     * You can pass several [FacetSortCriterion] in the [FacetListPresenter.sortBy] parameter.
     * Each of them will be applied. The order in which each of them is passed determines the priority of each sorting criteria.
     */
    val presenterFacetList = FacetListPresenter(sortBy = listOf(
        FacetSortCriterion.IsRefined,
        FacetSortCriterion.AlphabeticalDescending)
    )

    /**
     * This transform an [Index] into a String for presentation purposes.
     */
    val presenterSortBy: IndexPresenter = { index: Index ->
        when (index) {
            Kensium.index -> "Normal"
            Kensium.indexPriceAsc -> "Price Asc"
            else -> "Default"
        }
    }

    init {
        configureSearcherForFacets()
        configureSearcher()
        configurePriceFilter()
        configureBrandFilter()
        configureCategoryLvl1Filter()
        configureProduct()
        configureIndexSortBy()
    }

    private fun configureSearcherForFacets() {
        searcherForFacets.let {
            it.response?.let { response -> adapterCategoryLvl0.submitList(response.facets) }
            it.onResponseChanged += { response -> adapterCategoryLvl0.submitList(response.facets) }
            it.onErrorChanged += { error -> error.printStackTrace() }
            it.search()
        }
    }

    private fun configureSearcher() {
        searcher.let {
            it.onErrorChanged += { error -> error.printStackTrace() }
        }
    }

    private fun configureBrandFilter() {
        val brandViewModel = FacetListViewModel()

        brandViewModel.connectFilterState(Kensium.brand, searcher.filterState, Kensium.groupIDBrand)
        brandViewModel.connectSearcher(Kensium.brand, searcher)
        brandViewModel.connectView(adapterBrand, presenterFacetList)
    }

    private fun configureCategoryLvl1Filter() {
        val categoryViewModel = FacetListViewModel()

        categoryViewModel.connectFilterState(Kensium.categoryLvl1, searcher.filterState, Kensium.groupIDCategoryLvl1)
        categoryViewModel.connectSearcher(Kensium.categoryLvl1, searcher)
        categoryViewModel.connectView(adapterCategoryLvl1, presenterFacetList)
    }

    private fun configurePriceFilter() {
        val priceViewModel = FilterListViewModel.Numeric()
        /**
         * Add the price facet to access FacetStats later.
         */
        searcher.query.addFacet(Kensium.price)
        priceViewModel.connectFilterState(searcher.filterState, Kensium.groupIDPrice)
        priceViewModel.connectView(adapterPrice)

        searcher.onResponseChanged += { response ->
            /**
             * Here, use the facetStats to do dynamic computation of the price filters.
             */
            val facetStatsPrice = response.facetStats[Kensium.price]

            /**
             * Example of price filters. Use "facetStatsPrice" above to compute dynamic filter ranges, instead of static.
             */
            priceViewModel.items = listOf(
                Filter.Numeric(Kensium.price, NumericOperator.Less, 10),
                Filter.Numeric(Kensium.price, 10..20),
                Filter.Numeric(Kensium.price, 20..30),
                Filter.Numeric(Kensium.price, NumericOperator.Greater, 30)
            )
        }
    }

    private fun configureProduct() {
        searcher.onResponseChanged += { response ->
            adapterProduct.submitList(response.hits.deserialize(Product.serializer()))
        }
    }

    private fun configureIndexSortBy() {
        indexSegmentViewModel.connectSearcher(searcher)

    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        searcherForFacets.cancel()
    }
}