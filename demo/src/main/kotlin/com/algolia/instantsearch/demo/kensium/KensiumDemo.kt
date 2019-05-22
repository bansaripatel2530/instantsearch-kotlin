package com.algolia.instantsearch.demo.kensium

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.demo.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.filter.facet.FacetListViewModel
import com.algolia.instantsearch.helper.filter.facet.connectFilterState
import com.algolia.instantsearch.helper.filter.facet.connectSearcher
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_facet_list.*
import kotlinx.android.synthetic.main.header_filter.*


class KensiumDemo: AppCompatActivity() {

    val category = Attribute("categories.level0")
    val groupID = FilterGroupID.And(category)
    val filterState = FilterState(
        facetGroups = mutableMapOf(groupID to setOf(Filter.Facet(category, "Hair")))
    )
    val client = ClientSearch(ApplicationID("HDLGLOLEHS"), APIKey("75025d6975e4c9a012de7163cde4e42d"))
    val indexName = IndexName("production_default_products")
    val index = client.initIndex(indexName)
    val searcher = SearcherSingleIndex(index, filterState)
    val brand = Attribute("brand")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_facet_list)

        val brandViewModel = FacetListViewModel()
        val brandAdapter = FacetListAdapter()

        brandViewModel.connectFilterState(brand, searcher.filterState)
        brandViewModel.connectSearcher(brand, searcher)
        brandViewModel.connectView(brandAdapter)

        val categoryViewModel = FacetListViewModel(selectionMode = SelectionMode.Single)
        val categoryAdapter = FacetListAdapter()

        categoryViewModel.connectFilterState(category, searcher.filterState, groupID)
        categoryViewModel.connectSearcher(category, searcher)
        categoryViewModel.connectView(categoryAdapter)

        (listTopLeft as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = brandAdapter
            it.itemAnimator = null
        }

        (listTopRight as RecyclerView).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = categoryAdapter
            it.itemAnimator = null
        }

        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onChangeThenUpdateFiltersText(filterState, mapOf(), filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }
}