package org.turter.patrocl.data.local.entity.person

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class WaiterLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var code: String = ""
    var guid: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
}