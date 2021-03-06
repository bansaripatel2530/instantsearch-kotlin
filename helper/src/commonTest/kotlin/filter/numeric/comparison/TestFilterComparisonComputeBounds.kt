package filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.core.selectable.range.Range
import com.algolia.instantsearch.helper.filter.numeric.comparison.computeBoundsFromFacetStats
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.FacetStats
import shouldEqual
import kotlin.test.Test


class TestFilterComparisonComputeBounds {

    private val price = Attribute("price")
    private val facetStats = mapOf(
        price to FacetStats(min = 0f, max = 10f, sum = 10f, average = 5f)
    )

    @Test
    fun connectSearcherShouldUpdateBounds() {
        val viewModel = SelectableNumberViewModel.Int()

        viewModel.computeBoundsFromFacetStats(price, facetStats)
        viewModel.bounds shouldEqual Range.Int(0, 10)
    }
}