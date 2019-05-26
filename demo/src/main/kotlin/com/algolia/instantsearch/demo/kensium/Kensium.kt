package com.algolia.instantsearch.demo.kensium

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.search.model.Attribute


object Kensium  {

    val price = Attribute("price.USD.default")
    val brand = Attribute("brand")
    val categoryLvl0 = Attribute("categories.level0")
    val categoryLvl1 = Attribute("categories.level1")

    val groupIDCategoryLvl0 = FilterGroupID.And()
    val groupIDCategoryLvl1 = FilterGroupID.Or(categoryLvl1)
    val groupIDBrand = FilterGroupID.Or(brand)
    val groupIDPrice = FilterGroupID.And(price)
}