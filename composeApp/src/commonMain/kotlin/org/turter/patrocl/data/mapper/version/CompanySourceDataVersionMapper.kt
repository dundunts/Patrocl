package org.turter.patrocl.data.mapper.version

import org.turter.patrocl.data.dto.source.dataversion.CompanySourceDataVersion
import org.turter.patrocl.domain.model.enums.SourceDataType
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal

fun CompanySourceDataVersionLocal.toCompanySourceDataVersion(): CompanySourceDataVersion =
    CompanySourceDataVersion(
        dataType = SourceDataType.valueOf(dataType),
        companyId = companyId,
        count = count,
        version = version
    )

fun CompanySourceDataVersion.toCompanySourceDataVersionLocal(): CompanySourceDataVersionLocal =
    CompanySourceDataVersionLocal().let { target ->
        target.dataType = dataType.name
        target.companyId = companyId
        target.count = count
        target.version = version
        return@let target
    }