package org.turter.patrocl.data.local.entity.menu

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ModifierLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
    var maxOneDish: Int = 0
    var useLimitedQnt: Boolean = false
    var price: Int = 0
}