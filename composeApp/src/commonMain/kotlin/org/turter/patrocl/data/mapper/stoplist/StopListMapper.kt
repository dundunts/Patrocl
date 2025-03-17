package org.turter.patrocl.data.mapper.stoplist

import kotlinx.datetime.LocalDateTime
import org.turter.patrocl.data.dto.stoplist.StopListDto
import org.turter.patrocl.data.dto.stoplist.StopListItemDto
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.stoplist.StopList
import org.turter.patrocl.domain.model.stoplist.StopListItem

fun StopListDto.toStopList(dishes: List<StationDishInfo>): StopList = StopList(
    items = items.map { it.toStopListItem(dishes) }
)
//    when(status) {
//        StopListDto.Status.SUCCESS, StopListDto.Status.EMPTY -> StopList.Success(
//            items = items.map { it.toStopListItem(dishes) }
//        )
//
//        StopListDto.Status.ERROR -> StopList.Error(message = message)
//    }

fun StopListItemDto.toStopListItem(dishes: List<StationDishInfo>) = StopListItem(
    id = id,
    dishRkId = dishId,
    dishName = dishes.find { it.rkId == dishId }?.name?:"",
    onStop = onStop,
    remainingCount = remainingCount,
    until = until.let { time ->
        try {
            LocalDateTime.parse(time)
        } catch (e: Exception) {
            LocalDateTime(2077, 4, 1, 0, 0)
        }
    },
    createdAt = createdAt
)