package refinement.filters

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import refinement.filter.RefinementFilterPresenter
import selection.map.SelectableMapView
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestRefinementFiltersView {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")
    private val id = 0
    private val filters = mapOf(id to red)

    private class MockSelectableView : SelectableMapView<Int, String> {

        var int: Int? = null
        var map: Map<Int, String> = mapOf()

        override var onClick: ((Int) -> Unit)? = null

        override fun setSelected(selected: Int?) {
            int = selected
        }

        override fun setItems(items: Map<Int, String>) {
            map = items
        }
    }

    @Test
    fun connectShouldCallSetSelectedAndSetItems() {
        val view = MockSelectableView()
        val viewModel = RefinementFiltersViewModel(filters, id)

        viewModel.connectView(view)
        view.int shouldEqual id
        view.map shouldEqual mapOf(id to RefinementFilterPresenter(red))
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableView()
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.onSelectedComputed += { viewModel.selected = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(id)
        view.int shouldEqual id
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableView()
        val viewModel = RefinementFiltersViewModel(filters)

        viewModel.connectView(view)
        viewModel.selected = id
        view.int shouldEqual id
    }
}