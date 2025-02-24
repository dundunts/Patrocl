package org.turter.patrocl.data.mapper.menu

import io.realm.kotlin.ext.toRealmList
import org.turter.patrocl.data.dto.source.category.CategoryInfoDto
import org.turter.patrocl.data.local.entity.menu.CategoryLocal
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.deprecated.Category
import org.turter.patrocl.domain.model.menu.deprecated.CategoryDetailed
import org.turter.patrocl.domain.model.menu.deprecated.Dish
import org.turter.patrocl.domain.model.stoplist.StopListItem

fun CategoryInfoDto.toCategoryInfo(): CategoryInfo =
    CategoryInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        childIds = childIds,
        dishIds = dishIds
    )

fun CategoryInfoDto.toCategoryLocal(): CategoryLocal =
    CategoryLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.childIds = childIds.toRealmList()
        target.dishIds = dishIds.toRealmList()
        return@let target
    }

fun List<CategoryInfoDto>.toCategoryLocalList() = this.map { it.toCategoryLocal() }.toList()

fun CategoryLocal.toCategoryInfo(): CategoryInfo =
    CategoryInfo(
        id = id,
        rkId = rkId,
        guid = guid,
        code = code,
        name = name,
        status = status,
        mainParentIdent = mainParentIdent,
        childIds = childIds.toList(),
        dishIds = dishIds.toList()
    )

fun List<CategoryLocal>.toCategoryInfoList(): List<CategoryInfo> =
    this.map { it.toCategoryInfo() }.toList()

fun Category.toDetailed(
    parent: CategoryDetailed?,
    allDishes: List<Dish>,
    stopList: List<StopListItem>
): CategoryDetailed {
    val result = CategoryDetailed(
        id = this.id,
        guid = this.guid,
        code = this.code,
        name = this.name,
        status = this.status,
        parent = parent,
        childList = emptyList(),
        dishes = allDishes
            .filter { dish -> this.dishIdList.contains(dish.id) }
            .map { it.toDetailed(stopList) }
            .toList()
    )
    result.childList = this.childList
        .map { category -> category.toDetailed(result, allDishes, stopList) }
        .toList()
    return result
}