package com.algolia.instantsearch.demo.home

import com.algolia.instantsearch.demo.index.IndexSegmentDemo
import com.algolia.instantsearch.demo.list.paging.PagingSingleIndexDemo
import com.algolia.instantsearch.demo.search.SearchAsYouTypeDemo
import com.algolia.instantsearch.demo.search.SearchOnSubmitDemo
import com.algolia.search.model.ObjectID


val homeActivities = mapOf(
//    ObjectID("filter_toggle") to FilterToggleDemo::class,
//    ObjectID("filter_toggle_default") to FilterToggleDefaultDemo::class,
//    ObjectID("filter_segment") to FilterSegmentDemo::class,
//    ObjectID("filter_list_all") to FilterListAllDemo::class,
//    ObjectID("filter_list_numeric") to FilterListNumericDemo::class,
//    ObjectID("filter_list_facet") to FilterListFacetDemo::class,
//    ObjectID("filter_list_tag") to FilterListTagDemo::class,
//    ObjectID("facet_list") to FacetListDemo::class,
//    ObjectID("facet_list_persistent") to FacetListPersistentDemo::class,
//    ObjectID("facet_list_search") to FacetListSearchDemo::class,
//    ObjectID("filter_numeric_comparison") to FilterComparisonDemo::class,
        ObjectID("paging_single_searcher") to PagingSingleIndexDemo::class,
        ObjectID("search_on_submit") to SearchOnSubmitDemo::class,
        ObjectID("search_as_you_type") to SearchAsYouTypeDemo::class,
        ObjectID("paging_single_searcher") to PagingSingleIndexDemo::class,
        ObjectID("index_segment") to IndexSegmentDemo::class
)