package org.turter.patrocl.data.local.entity.menu

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DishLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
    var kurs: String = ""
    var qntDecDigits: Int = 0
    var modiScheme: String = ""
    var comboScheme: String = ""
    var categPath: String = ""
    var price: Int = 0
}