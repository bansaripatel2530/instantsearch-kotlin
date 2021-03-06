package com.algolia.instantsearch.helper.filter.state

import com.algolia.search.model.filter.Filter


internal data class FiltersImpl(
    private val facetGroups: Map<FilterGroupID, Set<Filter.Facet>>,
    private val tagGroups: Map<FilterGroupID, Set<Filter.Tag>>,
    private val numericGroups: Map<FilterGroupID, Set<Filter.Numeric>>
) : Filters {

    override fun getFacetFilters(groupID: FilterGroupID): Set<Filter.Facet> {
        return facetGroups[groupID].orEmpty()
    }

    override fun getTagFilters(groupID: FilterGroupID): Set<Filter.Tag> {
        return tagGroups[groupID].orEmpty()
    }

    override fun getNumericFilters(groupID: FilterGroupID): Set<Filter.Numeric> {
        return numericGroups[groupID].orEmpty()
    }

    override fun getFacetGroups(): Map<FilterGroupID, Set<Filter.Facet>> {
        return facetGroups
    }

    override fun getTagGroups(): Map<FilterGroupID, Set<Filter.Tag>> {
        return tagGroups
    }

    override fun getNumericGroups(): Map<FilterGroupID, Set<Filter.Numeric>> {
        return numericGroups
    }

    override fun getFilters(groupID: FilterGroupID): Set<Filter> {
            return getFacetFilters(groupID) + getTagFilters(groupID) + getNumericFilters(groupID)
    }

    override fun getFilters(): Set<Filter> {
        return (facetGroups.values + tagGroups.values + numericGroups.values).flatten().toSet()
    }

    override fun <T : Filter> contains(groupID: FilterGroupID, filter: T): Boolean {
        return when (filter) {
            is Filter.Facet -> facetGroups[groupID]?.contains(filter)
            is Filter.Tag -> tagGroups[groupID]?.contains(filter)
            is Filter.Numeric -> numericGroups[groupID]?.contains(filter)
            else -> null
        } ?: false
    }

    override fun toString(): String {
        return "FiltersImpl(facetGroups=$facetGroups, tagGroups=$tagGroups, numericGroups=$numericGroups)"
    }
}