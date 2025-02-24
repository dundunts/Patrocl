package org.turter.patrocl.data.remote.client.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import org.turter.patrocl.data.dto.source.category.CompanyCategoriesData
import org.turter.patrocl.data.dto.source.deprecated.CategoryDto
import org.turter.patrocl.data.dto.source.deprecated.DishDto
import org.turter.patrocl.data.dto.source.deprecated.ModifierDto
import org.turter.patrocl.data.dto.source.deprecated.ModifiersGroupDto
import org.turter.patrocl.data.dto.source.dish.CompanyStationDishesData
import org.turter.patrocl.data.dto.source.modifier.group.CompanyModifiersGroupsData
import org.turter.patrocl.data.dto.source.modifier.list.CompanyStationModifiersData
import org.turter.patrocl.data.dto.source.modifier.scheme.CompanyModifiersSchemesData
import org.turter.patrocl.data.remote.client.ApiEndpoint
import org.turter.patrocl.data.remote.client.MenuApiClient
import org.turter.patrocl.data.remote.client.proceedRequest

class MenuApiClientImpl(
    private val httpClient: HttpClient
): MenuApiClient {
    override suspend fun getAvailableStationDishesForUser(): Result<CompanyStationDishesData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Menu.getAvailableDishes()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getCategoriesForUser(): Result<CompanyCategoriesData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Menu.getCategoryData()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getAvailableStationModifiersForUser(): Result<CompanyStationModifiersData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Menu.getAvailableModifiers()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getModifiersGroupsForUser(): Result<CompanyModifiersGroupsData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Menu.getModifiersGroupData()) },
            decoder = { Json.decodeFromString(it.body()) }
        )

    override suspend fun getModifiersSchemesForUser(): Result<CompanyModifiersSchemesData> =
        proceedRequest(
            action = { httpClient.get(ApiEndpoint.Menu.getModifiersSchemesData()) },
            decoder = { Json.decodeFromString(it.body()) }
        )
}