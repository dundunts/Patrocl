package org.turter.patrocl.data_mock.utils

import org.turter.patrocl.domain.model.hall.HallInfo
import org.turter.patrocl.domain.model.hall.HallType
import org.turter.patrocl.domain.model.hall.HallsData
import org.turter.patrocl.domain.model.hall.TableInfo
import org.turter.patrocl.domain.model.hall.deprecated.Table

object TableDataSupplier {

    fun getTables() = listOf(
        Table(
            id = "table-id-1",
            guid = "table-guid-1",
            code = "table-code-1",
            name = "33",
            status = "ok",
            hall = "hall-id-root"
        ),
        Table(
            id = "table-id-2",
            guid = "table-guid-2",
            code = "table-code-2",
            name = "34",
            status = "ok",
            hall = "hall-id-root"
        ),
        Table(
            id = "table-id-3",
            guid = "table-guid-3",
            code = "table-code-3",
            name = "54",
            status = "ok",
            hall = "hall-id-root"
        ),
        Table(
            id = "table-id-4",
            guid = "table-guid-4",
            code = "table-code-4",
            name = "21",
            status = "ok",
            hall = "hall-id-root"
        )
    )

    fun createTables(hallId: String, numberOfTables: Int): List<TableInfo> {
        return (1..numberOfTables).map { index ->
            TableInfo(
                id = "table-$hallId-$index",
                rkId = "rk-${hallId}-${index}",
                guid = "guid-${hallId}-${index}",
                code = "code-${hallId}-${index}",
                name = "Table $index",
                status = "AVAILABLE",
                hall = hallId,
                tableGroup = "Group1"
            )
        }
    }

    fun createHall(id: String, rkId: String, name: String, hallType: HallType, numberOfTables: Int): HallInfo {
        val tables = createTables(id, numberOfTables)
        return HallInfo(
            id = id,
            rkId = rkId,
            guid = "guid-$rkId",
            code = "code-$rkId",
            name = name,
            status = "ACTIVE",
            mainParentIdent = "mainParentIdent",
            restaurant = "Restaurant Name",
            hallType = hallType,
            tables = tables
        )
    }

    val mainHall = createHall("mainHall", "rk-mainHall", "Основной", HallType.MAIN_HALL, 5)
    val verandaHall = createHall("verandaHall", "rk-verandaHall", "Веранда", HallType.NOT_SPECIFIED, 5)

    val hallsData = HallsData(
        defaultHallRkId = mainHall.rkId,
        halls = listOf(mainHall, verandaHall)
    )

}