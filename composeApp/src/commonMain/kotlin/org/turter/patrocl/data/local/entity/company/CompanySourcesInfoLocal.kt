package org.turter.patrocl.data.local.entity.company

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompanySourcesInfoLocal: RealmObject {
    @PrimaryKey
    var companyId: String = ""
    var rootCategoryRkId: String = ""
    var rootModifierGroupRkId: String = ""
    var defaultHallRkId: String = ""
}