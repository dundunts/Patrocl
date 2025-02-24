package org.turter.patrocl.data_mock.utils

import org.turter.patrocl.domain.model.person.Waiter

object WaiterDataSupplier {

    fun getWaiter() = Waiter(
        id = "waiter-id-1",
        rkId = "waiter-rkId-1",
        guid = "waiter-guid-1",
        code = "99",
        name = "Бобби"
    )

    fun getLoggedInWaiters() = listOf(
        Waiter(
            id = "waiter-id-1",
            rkId = "waiter-rkId-1",
            guid = "waiter-guid-1",
            code = "99",
            name = "Бобби"
        ),
        Waiter(
            id = "waiter-id-2",
            rkId = "waiter-rkId-2",
            guid = "waiter-guid-2",
            code = "133",
            name = "Билли"
        ),
        Waiter(
            id = "waiter-id-3",
            rkId = "waiter-rkId-3",
            guid = "waiter-guid-3",
            code = "28",
            name = "Биба"
        )
    )

}