package org.turter.patrocl.data.local.entity.version

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CompanySourceDataVersionLocal: RealmObject {
    @PrimaryKey
    var dataType: String = ""
    var companyId: String = ""
    var count: Long = 0
    var version: String = ""

    override fun toString(): String {
        return "CompanySourceDataVersionLocal(" +
                "dataType='$dataType', " +
                "companyId='$companyId', " +
                "count=$count, " +
                "version='$version'" +
                ")"
    }
}