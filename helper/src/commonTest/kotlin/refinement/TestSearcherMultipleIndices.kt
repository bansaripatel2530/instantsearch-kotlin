package refinement

import algolia
import blocking
import com.algolia.search.filter.setFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query
import indexA
import indexB
import searcher.SearcherMultipleIndices
import shouldEqual
import kotlin.test.Test


class TestSearcherMultipleIndices {

    @Test
    fun test() {
        blocking {
            val attribute1 = Attribute("brand")
            val attribute2 = Attribute("color")
            val query = Query().apply { setFacets(attribute1, attribute2) }
            val indexQuery0 = IndexQuery(indexA.indexName, query)
            val indexQuery1 = IndexQuery(indexB.indexName, query)
            val searcher = SearcherMultipleIndices(algolia, listOf(indexQuery0, indexQuery1))

            searcher.search()

            searcher.completed!!.await().let {
                it.results[0].facets.keys.size shouldEqual 2
                it.results[1].facets.keys.size shouldEqual 2
                it.results[0].indexName shouldEqual indexQuery0.indexName
                it.results[1].indexName shouldEqual indexQuery1.indexName
            }
        }
    }
}