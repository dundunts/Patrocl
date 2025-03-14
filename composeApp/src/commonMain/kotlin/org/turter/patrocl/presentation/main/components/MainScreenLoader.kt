package org.turter.patrocl.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.turter.patrocl.presentation.components.FullscreenLoader

@Composable
fun MainScreenLoader(modifier: Modifier = Modifier, supportingText: String? = null) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        FullscreenLoader(supportingText = supportingText)
    }
}