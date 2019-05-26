package com.algolia.instantsearch.demo.kensium

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel


class KensiumViewModel : ViewModel() {

    private val applicationID = ApplicationID("HDLGLOLEHS")
    private val apiKey = APIKey("75025d6975e4c9a012de7163cde4e42d")
    private val client = ClientSearch(applicationID, apiKey, LogLevel.BODY)
    private val indexName = IndexName("production_default_products")

    val index = client.initIndex(indexName)
    val searcher = SearcherSingleIndex(index)
    val searcherForFacets = SearcherForFacets(index, Kensium.categoryLvl0)

    init {
        searcherForFacets.search()
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
    }
}