package org.turter.patrocl.data.local.config

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.turter.patrocl.data.local.entity.company.CompanySourcesInfoLocal
import org.turter.patrocl.data.local.entity.hall.HallLocal
import org.turter.patrocl.data.local.entity.menu.CategoryLocal
import org.turter.patrocl.data.local.entity.person.CompanyEmbeddedLocal
import org.turter.patrocl.data.local.entity.menu.DishLocal
import org.turter.patrocl.data.local.entity.menu.ModifierLocal
import org.turter.patrocl.data.local.entity.person.EmployeeLocal
import org.turter.patrocl.data.local.entity.menu.ModifiersGroupLocal
import org.turter.patrocl.data.local.entity.person.PositionEmbeddedLocal
import org.turter.patrocl.data.local.entity.hall.TableLocal
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeDetailsLocal
import org.turter.patrocl.data.local.entity.menu.ModifiersSchemeLocal
import org.turter.patrocl.data.local.entity.person.WaiterLocal
import org.turter.patrocl.data.local.entity.version.CompanySourceDataVersionLocal
import org.turter.patrocl.data.local.entity.voids.OrderItemVoidLocal

object RealmManager {
    private val config = RealmConfiguration.Builder(
        schema = setOf(
            CompanySourcesInfoLocal::class,
            CompanySourceDataVersionLocal::class,
            DishLocal::class,
            CategoryLocal::class,
            ModifierLocal::class,
            ModifiersGroupLocal::class,
            ModifiersSchemeLocal::class,
            ModifiersSchemeDetailsLocal::class,
            HallLocal::class,
            TableLocal::class,
            WaiterLocal::class,
            OrderItemVoidLocal::class,
            EmployeeLocal::class,
            PositionEmbeddedLocal::class,
            CompanyEmbeddedLocal::class
        )
    )
        .compactOnLaunch()
        .build()

    private val instance: Realm by lazy {
        Realm.open(config)
    }

    fun getRealm(): Realm = instance

    fun closeRealm() = instance.close()
}