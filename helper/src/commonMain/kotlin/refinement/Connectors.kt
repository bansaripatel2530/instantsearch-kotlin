package refinement

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import refinement.RefinementMode.Conjunctive
import refinement.RefinementMode.Disjunctive
import search.Group
import search.SearchFilterState
import search.SearcherSingleIndex

enum class RefinementMode {
    Conjunctive,
    Disjunctive
}

fun RefinementListViewModel<Facet>.connectWith(
    searcher: SearcherSingleIndex,
    mode: RefinementMode,
    attribute: Attribute,
    groupName: String = attribute.raw
) {
    val group = when (mode) {
        Conjunctive -> Group.And.Facet(groupName)
        Disjunctive -> Group.Or.Facet(groupName)
    }

    searcher.responseListeners += { response ->
        response.facets[attribute]?.let { refinements = it }
    }
    searcher.filterState.stateListeners += { state ->
        val filters = state[group].orEmpty().filterIsInstance<Filter.Facet>().map { it.value }

        selected = refinements.filter { refinement -> filters.any { it.raw == refinement.value } }
    }
    selectionListeners += { facets ->
        val filters = facets.map { it.toFilter(attribute) }.toSet()

        searcher.filterState.replace(group, filters)
    }
}

fun Facet.toFilter(attribute: Attribute): Filter.Facet = Filter.Facet(attribute, this.value)


