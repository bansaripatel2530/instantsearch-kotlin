package com.algolia.instantsearch.demo.kensium

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.demo.filter.facet.FacetListAdapter
import com.algolia.instantsearch.demo.kensium.category.CategoryAdapter
import com.algolia.instantsearch.demo.kensium.filter.FilterPriceAdapter
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductPagedAdapter
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
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
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator


/**
 * This class will be shared with all fragments from the same activity.
 */
class KensiumViewModel : ViewModel() {

    val searcher = SearcherSingleIndex(Kensium.index)
    val searcherForFacets = SearcherForFacets(Kensium.index, Kensium.categoryLvl0)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher, Product.serializer())
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).build()
    val products = LivePagedListBuilder<Int, Product>(dataSourceFactory, pagedListConfig).build()
    /**
     * This is for formatting purposes: We replace "price.USD.default" by "price" when we display our list of Filter.Numeric.
     */
    val filterNumericPresenter = FilterPresenterImpl(AttributeMatchAndReplace(Kensium.price, "price"))

    val adapterCategoryLvl0 = CategoryAdapter()
    val adapterProduct = ProductPagedAdapter()
    val adapterBrand = FacetListAdapter()
    val adapterCategoryLvl1 = FacetListAdapter()
    val adapterPrice = FilterPriceAdapter(filterNumericPresenter)

    /**
     * Here you can define all the index you want. Each time a new index will be selected, the results will automatically change.
     * This achieve the "SortBy" feature.
     */
    val indexSegmentViewModel = IndexSegmentViewModel(items = mapOf(
            0 to Kensium.index,
            1 to Kensium.indexPriceAsc,
            2 to Kensium.indexPriceDes,
            3 to Kensium.indexNewestFirst,
            4 to Kensium.indexNameAscName,
            5 to Kensium.indexNameDesName
    ))

    val brandViewModel = FacetListViewModel()
    val categoryViewModel = FacetListViewModel()
    val priceViewModel = FilterListViewModel.Numeric()
    private var isClear = false
    /**
     * You can pass several [FacetSortCriterion] in the [FacetListPresenter.sortBy] parameter.
     * Each of them will be applied. The order in which each of them is passed determines the priority of each sorting criteria.
     */
    val list = listOf(
            FacetSortCriterion.CountDescending,
            FacetSortCriterion.AlphabeticalDescending)
    val presenterFacetList = FacetListPresenter(list, 1000000)

    /**
     * This transform an [Index] into a String for presentation purposes.
     */
    val presenterSortBy: IndexPresenter = { index: Index ->
        when (index) {
            Kensium.index -> "Relevance"
            Kensium.indexPriceAsc -> "Lowest Price"
            Kensium.indexPriceDes -> "Highest Price"
            Kensium.indexNewestFirst -> "Newest First"
            Kensium.indexNameAscName -> "Name Asc"
            Kensium.indexNameDesName -> "Name Des"
            else -> "Default"
        }
    }

    init {
        configureSearcherForFacets()
        configureSearcher()
        configurePriceFilter()
        configureBrandFilter()
        configureCategoryLvl1Filter()
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
        brandViewModel.connectFilterState(Kensium.brand, searcher.filterState, Kensium.groupIDBrand)
        brandViewModel.connectSearcher(Kensium.brand, searcher)
        brandViewModel.connectView(adapterBrand, presenterFacetList)


    }

    private fun configureCategoryLvl1Filter() {
        categoryViewModel.connectFilterState(Kensium.categoryLvl1, searcher.filterState, Kensium.groupIDCategoryLvl1)
        categoryViewModel.connectSearcher(Kensium.categoryLvl1, searcher)
        categoryViewModel.connectView(adapterCategoryLvl1, presenterFacetList)
    }

    private fun configurePriceFilter() {
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
            if(searcher.filterState.getNumericFilters(Kensium.groupIDPrice).isEmpty()){
                isClear = true
                setDropDown(facetStatsPrice?.min?.toDouble()!!,facetStatsPrice.max.toDouble())
            }

            if(searcher.filterState.getFacetFilters(Kensium.groupIDBrand).isNotEmpty()
                    || searcher.filterState.getFacetFilters(Kensium.groupIDCategoryLvl1).isNotEmpty()){
                 if(!isClear){
                     searcher.filterState.notify {
                         clear(Kensium.groupIDPrice)
                     }
                 }

                setDropDown(facetStatsPrice?.min?.toDouble()!!,facetStatsPrice.max.toDouble())
            }

        }
    }

    private fun setDropDown(min: Double, max: Double) {

        val minValue: Double
        val priceList = arrayListOf<String>()
        priceList.add(0, "Select price")
        if (min < 10.0) {
            minValue = 10.0
            priceList.add(1, "10")

        } else {
            minValue = round(min.toInt()).toDouble()
            priceList.add(1, "${minValue.toInt()}")

        }
        var maxValue: Int
        if (max.toInt() > 100) {
            maxValue = max.toInt() - max.toInt() % 100
        } else {
            maxValue = max.toInt() - max.toInt() % 10
            if (maxValue <= minValue) {
                maxValue = max.toInt()
            }
        }
        val remainingValue = maxValue - minValue
        var stepSize = 0
        if (remainingValue > 10) {
            stepSize = if ((remainingValue / 10).toInt() > 10) {
                round((remainingValue / 10).toInt())
            } else {
                10
                //                priceList.add("$${minValue.toInt()}-$${maxValue}")
            }
        } else {
            if (remainingValue > 0) {
                priceList.add("${minValue.toInt()}..$maxValue")
            }
        }
        if (stepSize > 0) {

            var index = 1
            for (i in minValue.toInt() until maxValue step stepSize) {
                index = index + 1
                if (i + stepSize < maxValue) {
                    priceList.add(index, "$i..${i + stepSize}")
                } else {
                    break
                }
            }


        }
        if (minValue < maxValue) {
            priceList.add(priceList.size, "$maxValue")

        }

        val list = arrayListOf<Filter.Numeric>()
        list.add(0, Filter.Numeric(Kensium.price, NumericOperator.Less, priceList[1].toDouble()))
        for (item in 2 until priceList.size - 1) {
            list.add(Filter.Numeric(Kensium.price, LongRange(priceList[item].split("..")[0].toLong()
                    , priceList[item].split("..")[1].toLong())))
        }
        list.add(Filter.Numeric(Kensium.price, NumericOperator.Greater, priceList[priceList.size - 1].toDouble()))


        priceViewModel.items = list


    }

    private fun round(n: Int): Int {
        val a = n / 10 * 10
        val b = a + 10
        return if (n - a > b - n) b else a
    }

    private fun configureProduct() {

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