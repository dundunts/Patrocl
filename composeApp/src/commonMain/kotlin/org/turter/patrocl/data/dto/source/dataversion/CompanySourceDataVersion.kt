package org.turter.patrocl.data.dto.source.dataversion

import kotlinx.serialization.Serializable
import org.turter.patrocl.data.dto.enums.SourceDataType

@Serializable
data class CompanySourceDataVersion(
    val dataType: SourceDataType,
    val companyId: String,
    val count: Long,
    val version: String
) {
    companion object {
        fun forDishes(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_STATION_DISHES,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forCategories(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_CATEGORIES,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forModifiers(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_STATION_MODIFIERS,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forModifiersGroup(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_MODIFIERS_GROUPS,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forModifiersScheme(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_MODIFIERS_SCHEMES,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forModifiersSchemeDetails(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_MODIFIERS_SCHEMES_DETAILS,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forHalls(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_HALLS,
                companyId = companyId,
                count = count,
                version = version
            )

        fun forOrderItemVoids(companyId: String, count: Long, version: String) =
            CompanySourceDataVersion(
                dataType = SourceDataType.COMPANY_ORDER_ITEM_VOIDS,
                companyId = companyId,
                count = count,
                version = version
            )
    }
}
