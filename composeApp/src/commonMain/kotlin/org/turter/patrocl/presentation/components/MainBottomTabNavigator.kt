package org.turter.patrocl.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import org.turter.patrocl.presentation.main.OrdersTab
import org.turter.patrocl.presentation.main.ProfileTab
import org.turter.patrocl.presentation.main.StopListTab

@Composable
fun MainBottomTabNavigator() {
    val tabs = listOf(
        StopListTab,
        OrdersTab,
        ProfileTab
    )

    NavigationBar(
        modifier = Modifier.height(80.dp)
    ) {
        tabs.forEach { TabNavigatorItem(it) }
    }
}

@Composable
private fun RowScope.TabNavigatorItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        label = { Text(tab.options.title) },
        icon = {
            val iconPainter =
                tab.options.icon ?: rememberVectorPainter(Icons.Default.FavoriteBorder)
            Icon(painter = iconPainter, contentDescription = tab.options.title)
        }
    )
}