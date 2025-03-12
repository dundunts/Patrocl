package org.turter.patrocl.data.local.entity.menu

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ModifiersSchemeDetailsLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var name: String = ""
    var modifiersGroupRkId: String = ""
    var defaultModifier: String = ""
    var upLimit: Int = 0
    var downLimit: Int = 0
    var freeCount: Boolean = true
    var useUpLimit: Boolean = true
    var useDownLimit: Boolean = true
}