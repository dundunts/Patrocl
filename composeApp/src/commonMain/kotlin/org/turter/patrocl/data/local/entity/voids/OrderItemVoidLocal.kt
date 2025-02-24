package org.turter.patrocl.data.local.entity.voids

import io.realm.kotlin.types.RealmObject

class OrderItemVoidLocal: RealmObject {
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
}