package org.turter.patrocl.data.local.entity.hall

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TableLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var hall: String = ""
    var tableGroup: String = ""
}