package org.turter.patrocl.data.local.entity.menu

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CategoryLocal: RealmObject {
    @PrimaryKey
    var id: String = ""
    var rkId: String = ""
    var guid: String = ""
    var code: String = ""
    var name: String = ""
    var status: String = ""
    var mainParentIdent: String = ""
    var childIds: RealmList<String> = realmListOf()
    var dishIds: RealmList<String> = realmListOf()
}