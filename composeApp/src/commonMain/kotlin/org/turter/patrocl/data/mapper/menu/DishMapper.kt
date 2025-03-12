package org.turter.patrocl.data.mapper.menu

import org.turter.patrocl.data.dto.source.dish.CompanyStationDishInfoDto
import org.turter.patrocl.data.local.entity.menu.DishLocal
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.menu.deprecated.Dish
import org.turter.patrocl.domain.model.menu.deprecated.DishDetailed
import org.turter.patrocl.domain.model.stoplist.StopListItem

fun CompanyStationDishInfoDto.toDishLocal(): DishLocal =
    DishLocal().let { target ->
        target.id = id
        target.rkId = rkId
        target.guid = guid
        target.code = code
        target.name = name
        target.status = status
        target.mainParentIdent = mainParentIdent
        target.kurs = kurs
        target.qntDecDigits = qntDecDigits
        target.modiScheme = modiScheme
        target.comboScheme = comboScheme
        target.categPath = categPath
        target.price = price
        return@let target
    }

fun List<CompanyStationDishInfoDto>.toDishListFromDto(): List<StationDishInfo> =
    map { dto ->
        StationDishInfo(
            id = dto.id,
            rkId = dto.rkId,
            guid = dto.guid,
            code = dto.code,
            name = dto.name,
            status = dto.status,
            mainParentIdent = dto.mainParentIdent,
            kurs = dto.kurs,
            qntDecDigits = dto.qntDecDigits,
            modiScheme = dto.modiScheme,
            comboScheme = dto.comboScheme,
            categPath = dto.categPath,
            price = dto.price
        )
    }.toList()

fun List<CompanyStationDishInfoDto>.toDishLocalList(): List<DishLocal> =
    map { it.toDishLocal() }.toList()

fun DishLocal.toStationDishInfo(): StationDishInfo =
    StationDishInfo(
        id = this.id,
        rkId = this.rkId,
        guid = this.guid,
        code = this.code,
        name = this.name,
        status = this.status,
        mainParentIdent = this.mainParentIdent,
        kurs = this.kurs,
        qntDecDigits = this.qntDecDigits,
        modiScheme = this.modiScheme,
        comboScheme = this.comboScheme,
        categPath = this.categPath,
        price = this.price
    )

fun List<DishLocal>.toStationDishInfoList(): List<StationDishInfo> =
    this.map { it.toStationDishInfo() }.toList()

fun Dish.toDetailed(stopList: List<StopListItem>): DishDetailed {
    val stopListItem = stopList.find { item -> item.dishRkId == this.id }
    return DishDetailed(
        id = this.id,
        guid = this.guid,
        code = this.code,
        name = this.name,
        status = this.status,
        mainParentIdent = this.mainParentIdent,
        onStop = stopListItem?.onStop ?: false,
        remainingCount = stopListItem?.remainingCount ?: Int.MAX_VALUE
    )
}