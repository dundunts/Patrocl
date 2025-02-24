package org.turter.patrocl.data.remote.client

import io.ktor.http.encodeURLPath
import org.turter.patrocl.data.remote.client.ApiServiceName.ORDER_SERVICE
import org.turter.patrocl.data.remote.client.ApiServiceName.ORGANIZATION_SERVICE
import org.turter.patrocl.data.remote.client.ApiServiceName.SOURCE_SERVICE
import org.turter.patrocl.data.remote.client.ApiServiceName.STOP_LIST_SERVICE

object ApiServiceName {
//    val STATION_CONNECTOR = "station-connector"
    val SOURCE_SERVICE = "source-service/api/v1"
    val ORDER_SERVICE = "order-service/api/v1"
    val ORGANIZATION_SERVICE = "organization-service"
    val NOTIFICATION_SERVICE = "notification-service"
    val STOP_LIST_SERVICE = "stoplist-service/api/v1"
}

//TODO актуализировать эндпоинты
object ApiEndpoint {
    private val API_BASE_URL_HTTP = "http://92.255.107.65:8765"
//    private val API_BASE_URL_WS = "ws://192.168.0.105:18765"

    object Menu {
        fun getAvailableDishes() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/dish/for-user/current/available"
        fun getCategoryTree() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/category/for-user/current/tree"
        fun getCategoryData() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/category/for-user/current/data"

        fun getAvailableModifiers() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/modifier/for-user/current/available"
        fun getModifiersGroupTree() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/modifiers-group/for-user/current/tree"
        fun getModifiersGroupData() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/modifiers-group/for-user/current/data"
        fun getModifiersSchemesData() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/modifier-scheme/for-user/current/data"
    }

    object OrderItemVoids {
        fun getOrderItemVoidsData() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/order-item-void/for-user/current/data"
    }

    object StopList {
        fun getStopList() = "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list/list"
        fun getStopListFlow() = "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list/flow"
        fun createStopListItem() = "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list"
        fun editStopListItem() = "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list"
        fun removeStopListItem(id: String) =
            "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list/$id".encodeURLPath()
        fun removeStopListItems() = "$API_BASE_URL_HTTP/$STOP_LIST_SERVICE/stop-list/items/remove"
    }

    object Hall {
        fun getHallsData() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/hall/for-user/current/data"
    }

    object Order {
        fun getOrder(guid: String) = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/$guid".encodeURLPath()
        fun getOpenedOrdersList() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/list/opened"
        fun getOpenedOrdersListFlow() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/flow/opened"
        fun createOrder() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/create"
        fun addItemsToOrder() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/session/items/add"
        fun removeItemsFromOrder() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/session/items/remove"
        fun updateOrderInfo() = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/update-info"
//        fun removeItem(guid: String) = "$API_BASE_URL_HTTP/$ORDER_SERVICE/order/$guid/remove-item".encodeURLPath()
//        fun getWebSocketOrderList() = "$API_BASE_URL_WS/$STATION_CONNECTOR/order/list/ws"
    }

    object Employee {
        fun getOwnEmployee() = "$API_BASE_URL_HTTP/$ORGANIZATION_SERVICE/employee/own"
        fun editOwnEmployee() = "$API_BASE_URL_HTTP/$ORGANIZATION_SERVICE/employee/own"
    }

    object Waiter {
        fun getOwnWaiter() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/waiter/for-user/current/own-waiter"
        fun getLoggedInWaitersInSameStation() = "$API_BASE_URL_HTTP/$SOURCE_SERVICE/waiter/station/current/logged-in"
//        fun editOwnEmployee() = "$API_BASE_URL_HTTP/$STATION_CONNECTOR/waiter/own"
    }
}