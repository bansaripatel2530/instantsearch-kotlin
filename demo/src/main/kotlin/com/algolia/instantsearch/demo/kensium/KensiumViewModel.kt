package com.algolia.instantsearch.demo.kensium

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.filter.facet.FacetListViewHolderImpl
import com.algolia.instantsearch.demo.kensium.category.CategoryAdapter
import com.algolia.instantsearch.demo.kensium.filter.FilterPriceAdapter
import com.algolia.instantsearch.demo.kensium.product.Product
import com.algolia.instantsearch.demo.kensium.product.ProductAdapter
import com.algolia.instantsearch.demo.kensium.product.ProductAdapterOne
import com.algolia.instantsearch.demo.kensium.subcategory.SubCategoryAdapter
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.attribute.AttributeMatchAndReplace
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.*
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.FilterListViewModel
import com.algolia.instantsearch.helper.filter.list.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.hierarchical.HierarchicalConnector
import com.algolia.instantsearch.helper.index.IndexPresenter
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.model.response.ResponseSearch

/**
 * This class will be shared with all fragments from the same activity.
 */
class KensiumViewModel : ViewModel() {

    val showErrorDialog = MutableLiveData<String>()
    val filterState = FilterState()
    val searcher = SearcherSingleIndex(Kensium.index)
    val brandCount = MutableLiveData<Int>()
    val cateCount = MutableLiveData<Int>()
    val priceCount = MutableLiveData<Int>()
    var connection = ConnectionHandler()
//
    val searcherForFacets = SearcherForFacets(Kensium.index, Kensium.categoryLvl0)
    val searchForSubCatFacets = SearcherForFacets(Kensium.index, Kensium.categoryLvl0)

     val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(Product.serializer()) }
     val pagedListConfig = PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
        val product:LiveData<PagedList<Product>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
     val searchBox = SearchBoxConnectorPagedList(searcher, listOf(product))
//    val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    val filterNumericPresenter = FilterPresenterImpl(AttributeMatchAndReplace(Kensium.price, "price"))

    val adapterCategoryLvl0 = CategoryAdapter()
    val adapterCategoryLvl2 = SubCategoryAdapter()
    val adapterProduct = ProductAdapter()
    val adapterProductOne = ProductAdapterOne()

    val adapterPrice = FilterPriceAdapter(filterNumericPresenter)
    private val brand = Attribute("brand")
    private val categories = Attribute("categories.level0")
    private val hierarchicalCategory = Attribute("categories")
    private val hierarchicalCategoryLvl0 = Attribute("$hierarchicalCategory.level0")
    private val hierarchicalCategoryLvl1 = Attribute("$hierarchicalCategory.level1")
    private val hierarchicalAttributes = listOf(
        hierarchicalCategoryLvl0,
        hierarchicalCategoryLvl1
    )
    val separator = " /// "
    private val price = Attribute("price.USD.default")
    private val groupBrand = groupAnd(brand)
    private val groupCategories = groupAnd(categories)
     val groupPrice = groupAnd(price)
    val hierarchical = HierarchicalConnector(
        searcher,
        hierarchicalCategory,
        filterState,
        hierarchicalAttributes,
        separator)

    private val filters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val filterList = FilterListConnector.Numeric(filters, filterState, groupID = groupPrice)

    val brandList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = brand,
        selectionMode = SelectionMode.Multiple,
        persistentSelection = true
    )
     val categoryList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = categories,
        selectionMode = SelectionMode.Single,
        groupID = groupCategories

    )

    val priceList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = price ,
        selectionMode = SelectionMode.Multiple
    )
     val brandPresenter = FacetListPresenterImpl(listOf(
        FacetSortCriterion.CountDescending
    ))
     val brandAdapter = FacetListAdapter(FacetListViewHolderImpl.Factory)
     val pricePresenter = FacetListPresenterImpl()
     val priceAdapter = FacetListAdapter(FacetListViewHolderImpl.Factory)
     val categoryPresenter = FacetListPresenterImpl(listOf(
        FacetSortCriterion.IsRefined
    ))
     val categoryAdapter =FacetListAdapter(FacetListViewHolderImpl.Factory)

    val brandViewModel = FacetListViewModel()
    val categoryViewModel = FacetListViewModel()
    val priceViewModel = FilterListViewModel.Numeric()
    val searchBoxViewModel = SearchBoxViewModel()

    /**
     * You can pass several [FacetSortCriterion] in the [FacetListPresenter.sortBy] parameter.
     * Each of them will be applied. The order in which each of them is passed determines the priority of each sorting criteria.
     */
    //TODO method name change
    val presenterFacetList = FacetListPresenterImpl(
        listOf(
            FacetSortCriterion.AlphabeticalDescending), 1000000
    )

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
//        configureSubCategoryLvl1Filter()
        configureCategoryLvl1Filter()
        configureIndexSortBy()
        configureSearchBox()
            connection += searchBox

    }


    private fun configureSearcherForFacets() {
//        searcherForFacets.let {
//            it.response?.let { response -> adapterCategoryLvl0.submitList(response.facets) }
//            it.onResponseChanged += { response -> adapterCategoryLvl0.submitList(response.facets) }
//            it.onErrorChanged += { error -> showErrorDialog.postValue(error.message) }
//            it.search()
//        }
    }

    private fun configureSearcher() {
//        searcher.let {
//            it.onErrorChanged += { error -> error.printStackTrace() }
//        }
    }

    private fun configureBrandFilter() {
//        brandViewModel.connectFilterState(Kensium.brand, filterState, Kensium.groupIDBrand)
//        brandViewModel.connectSearcher(Kensium.brand, searcher)
//        brandViewModel.connectView(adapterBrand, presenterFacetList)
    }

    private fun configureCategoryLvl1Filter() {
//        categoryViewModel.connectFilterState(Kensium.categoryLvl1, filterState, Kensium.groupIDCategoryLvl1)
//        categoryViewModel.connectSearcher(Kensium.categoryLvl1, searcher)
//        categoryViewModel.connectView(adapterCategoryLvl1, presenterFacetList)
    }


    private fun configurePriceFilter() {

//            searcher.query.addFacet(Kensium.price)
//        priceViewModel.connectFilterState(filterState, Kensium.groupIDPrice)
//        priceViewModel.connectView(adapterPrice)
        searcher.response.subscribe {
            if (it == null || it.hitsOrNull.isNullOrEmpty()) {

            } else {
                val facetStatsPrice = it.facetStats[Kensium.price]
                if (filterState.getNumericFilters(Kensium.groupIDPrice).isEmpty()) {
                    setDropDown(facetStatsPrice?.min?.toDouble()!!, facetStatsPrice.max.toDouble())
                }
//
//                if(filterState.getFacetFilters(Kensium.groupIDBrand).isNotEmpty()){
//                    if (response.facetsOrNull!!.get(Attribute("brand"))?.size!! > 0) {
//                        brandCount.postValue(-1)
//                    }else{
//                        brandCount.postValue(1)
//                    }
//                }
//
//                if(filterState.getFacetFilters(Kensium.groupIDCategoryLvl1).isNotEmpty()){
//                    if (response.facetsOrNull!!.get(Attribute("categories.level1"))?.size!! > 0) {
//                        cateCount.postValue(-1)
//                    }else{
//                        cateCount.postValue(1)
//                    }
//                }
//
//                if (filterState.getFacetFilters(Kensium.groupIDBrand).isNotEmpty()
//                        || filterState.getFacetFilters(Kensium.groupIDCategoryLvl1).isNotEmpty()) {
//                    if (!isClear) {
//                        filterState.notify {
//                            clear(Kensium.groupIDPrice)
//                        }
//                    }
//
//                    setDropDown(facetStatsPrice?.min?.toDouble()!!, facetStatsPrice.max.toDouble())
//                }
//            }
            }
        }
    }

    var priceFilterList : ArrayList<Filter.Numeric> ?= null
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
        Log.e("Price",""+priceList)
//        list.add(0, Filter.Numeric(Kensium.price, NumericOperator.Less, priceList[1].toDouble()))
//        for (item in 2 until priceList.size - 1) {
//            list.add(Filter.Numeric(Kensium.price, LongRange(priceList[item].split("..")[0].toLong()
//                    , priceList[item].split("..")[1].toLong())))
//        }
//        list.add(Filter.Numeric(Kensium.price, NumericOperator.Greater, priceList[priceList.size - 1].toDouble()))


//        priceViewModel.items = list

        priceFilterList = arrayListOf()
            priceFilterList!!.add(0,Filter.Numeric(price, NumericOperator.Less, priceList[1].toDouble()))
        for (item in 2 until priceList.size - 1) {
            priceFilterList!!.add(Filter.Numeric(Kensium.price, LongRange(priceList[item].split("..")[0].toLong()
                    , priceList[item].split("..")[1].toLong())))
        }
        priceFilterList!!.add(Filter.Numeric(Kensium.price, NumericOperator.Greater, priceList[priceList.size - 1].toDouble()))



    }

    private fun round(n: Int): Int {
        val a = n / 10 * 10
        val b = a + 10
        return if (n - a > b - n) b else a
    }

    private fun configureIndexSortBy() {
        /**
         * Invalidating the data source allows the RecyclerView using [SearcherSingleIndexDataSource] to reload itself.
         * This is necessary when we change index with an [IndexSegmentViewModel].
         */
//        indexSegmentViewModel.onSelectedComputed += { index ->
//            indexSegmentViewModel.items[index]?.let { searcher.index = it }
//            products.value?.dataSource?.invalidate()
//        }
    }

    private fun configureSearchBox() {
        searchBoxViewModel.connectSearcher(searcher, SearchMode.AsYouType)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        searcherForFacets.cancel()
    }
}