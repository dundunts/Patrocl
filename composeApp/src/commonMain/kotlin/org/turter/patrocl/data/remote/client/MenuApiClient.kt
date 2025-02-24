package org.turter.patrocl.data.remote.client

import org.turter.patrocl.data.dto.source.category.CompanyCategoriesData
import org.turter.patrocl.data.dto.source.dish.CompanyStationDishesData
import org.turter.patrocl.data.dto.source.modifier.group.CompanyModifiersGroupsData
import org.turter.patrocl.data.dto.source.modifier.list.CompanyStationModifiersData
import org.turter.patrocl.data.dto.source.modifier.scheme.CompanyModifiersSchemesData
import org.turter.patrocl.data.dto.source.voids.CompanyOrderItemVoidsData

//TODO remove comment methods
interface MenuApiClient {
//    suspend fun getCategoryTree(): Result<CategoryDto>
//    suspend fun getModifiersGroupTree(): Result<ModifiersGroupDto>
//    suspend fun getDishes(): Result<List<DishDto>>
//    suspend fun getModifiers(): Result<List<ModifierDto>>

    suspend fun getAvailableStationDishesForUser(): Result<CompanyStationDishesData>
    suspend fun getCategoriesForUser(): Result<CompanyCategoriesData>
    suspend fun getAvailableStationModifiersForUser(): Result<CompanyStationModifiersData>
    suspend fun getModifiersGroupsForUser(): Result<CompanyModifiersGroupsData>
    suspend fun getModifiersSchemesForUser(): Result<CompanyModifiersSchemesData>

}