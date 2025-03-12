package org.turter.patrocl.presentation.orders.read.components

import androidx.compose.runtime.Composable
import org.turter.patrocl.domain.model.order.Order
import org.turter.patrocl.presentation.orders.read.ReadOrderScreenState
import org.turter.patrocl.presentation.orders.read.ReadOrderViewModel

@Composable
fun ReadOrderComponent(
    state: ReadOrderScreenState.Main,
    vm: ReadOrderViewModel
) {
//    val originOrder = state.order
//    val sessions = originOrder.sessions
//
//    Scaffold(
//        topBar = {
//            ReadOrderTopAppBar(
//                orderName = originOrder.name,
//                waiterName = originOrder.waiter.name,
//                onBack = { vm.sendEvent(ReadOrderUiEvent.BackToOrders) }
//            )
//        }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            LazyColumn(
//                modifier = Modifier.fillMaxHeight(),
//                verticalArrangement = Arrangement.spacedBy(6.dp)
//            ) {
//                item(key = "s-0") {
//                    OrderItemsGroupsDivider(
//                        modifier = Modifier
//                            .animateItem()
//                            .padding(top = 6.dp, start = 2.dp, end = 2.dp),
//                        title = "Сохраненные позиции"
//                    )
//                }
//                sessions.forEach { session ->
//                    item(key = session.uni) {
//                        SessionCard(
//                            modifier = Modifier
//                                .animateItem()
//                                .padding(horizontal = 8.dp),
//                            session = session
//                        )
//                    }
//                    items(
//                        items = session.dishes.filter { it.quantity > 0 }.toList(),
//                        key = { it.getKey(session.uni) }
//                    ) { orderItem ->
//                        SavedOrderItemCard(
//                            modifier = Modifier
//                                .animateItem()
//                                .padding(horizontal = 16.dp),
//                            item = orderItem
//                        )
//                    }
//                }
//            }
//        }
//    }
}

private fun Order.Dish.getKey(sessionUni: String) = "$sessionUni-$uni"