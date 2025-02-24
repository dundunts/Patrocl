package org.turter.patrocl.data.local.entity.hall

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class HallLocal:RealmObject {
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
    var restaurant: String = ""
    var hallType: String = ""
    var tables: RealmList<TableLocal> = realmListOf()
}