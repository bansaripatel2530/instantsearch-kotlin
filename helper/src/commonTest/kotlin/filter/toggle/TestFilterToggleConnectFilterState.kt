package filter.toggle

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.helper.filter.toggle.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import kotlin.test.Test


class TestFilterToggleConnectFilterState {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val blue = Filter.Facet(color, "blue")
    private val groupID = FilterGroupID.And(color)
    private val expectedFilterState = FilterState(facetGroups = mutableMapOf(groupID to setOf(red)))
    private val expectedFilterStateDefault = FilterState(facetGroups = mutableMapOf(groupID to setOf(blue)))

    @Test
    fun connectShouldUpdateIsSelectedWithFilterState() {
        val viewModel = FilterToggleViewModel(red)

        viewModel.connectFilterState(expectedFilterState, groupID = groupID)
        viewModel.isSelected shouldEqual true
    }

    @Test
    fun onSelectionsComputedShouldUpdateFilterState() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID = groupID)
        viewModel.computeIsSelected(true)
        filterState shouldEqual expectedFilterState
    }

    @Test
    fun onFilterStateChangedShouldUpdateSelections() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, groupID = groupID)
        filterState.notify { add(groupID, red) }
        viewModel.isSelected shouldEqual true
    }

    @Test
    fun connectWithDefaultShouldUpdateFilterState() {
        val viewModel = FilterToggleViewModel(red)
        val filterState = FilterState()

        viewModel.connectFilterState(filterState, blue, groupID)
        filterState shouldEqual expectedFilterStateDefault
        viewModel.computeIsSelected(true)
        filterState shouldEqual expectedFilterState
        viewModel.computeIsSelected(false)
        filterState shouldEqual expectedFilterStateDefault
    }
}