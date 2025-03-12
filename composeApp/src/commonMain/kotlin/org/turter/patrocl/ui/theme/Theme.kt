package org.turter.patrocl.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import org.turter.patrocl.ui.theme.lazur.backgroundDark
import org.turter.patrocl.ui.theme.lazur.backgroundDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.backgroundDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.backgroundLight
import org.turter.patrocl.ui.theme.lazur.backgroundLightHighContrast
import org.turter.patrocl.ui.theme.lazur.backgroundLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.errorContainerDark
import org.turter.patrocl.ui.theme.lazur.errorContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.errorContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.errorContainerLight
import org.turter.patrocl.ui.theme.lazur.errorContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.errorContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.errorDark
import org.turter.patrocl.ui.theme.lazur.errorDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.errorDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.errorLight
import org.turter.patrocl.ui.theme.lazur.errorLightHighContrast
import org.turter.patrocl.ui.theme.lazur.errorLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceDark
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceLight
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceLightHighContrast
import org.turter.patrocl.ui.theme.lazur.inverseOnSurfaceLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.inversePrimaryDark
import org.turter.patrocl.ui.theme.lazur.inversePrimaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.inversePrimaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.inversePrimaryLight
import org.turter.patrocl.ui.theme.lazur.inversePrimaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.inversePrimaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceDark
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceLight
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceLightHighContrast
import org.turter.patrocl.ui.theme.lazur.inverseSurfaceLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onBackgroundDark
import org.turter.patrocl.ui.theme.lazur.onBackgroundDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onBackgroundDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onBackgroundLight
import org.turter.patrocl.ui.theme.lazur.onBackgroundLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onBackgroundLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onErrorContainerDark
import org.turter.patrocl.ui.theme.lazur.onErrorContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onErrorContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onErrorContainerLight
import org.turter.patrocl.ui.theme.lazur.onErrorContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onErrorContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onErrorDark
import org.turter.patrocl.ui.theme.lazur.onErrorDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onErrorDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onErrorLight
import org.turter.patrocl.ui.theme.lazur.onErrorLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onErrorLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerDark
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerLight
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryDark
import org.turter.patrocl.ui.theme.lazur.onPrimaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryLight
import org.turter.patrocl.ui.theme.lazur.onPrimaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onPrimaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerDark
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerLight
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryDark
import org.turter.patrocl.ui.theme.lazur.onSecondaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryLight
import org.turter.patrocl.ui.theme.lazur.onSecondaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onSecondaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceDark
import org.turter.patrocl.ui.theme.lazur.onSurfaceDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceLight
import org.turter.patrocl.ui.theme.lazur.onSurfaceLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantDark
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantLight
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onSurfaceVariantLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerDark
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerLight
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryDark
import org.turter.patrocl.ui.theme.lazur.onTertiaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryLight
import org.turter.patrocl.ui.theme.lazur.onTertiaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.onTertiaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.outlineDark
import org.turter.patrocl.ui.theme.lazur.outlineDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.outlineDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.outlineLight
import org.turter.patrocl.ui.theme.lazur.outlineLightHighContrast
import org.turter.patrocl.ui.theme.lazur.outlineLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.outlineVariantDark
import org.turter.patrocl.ui.theme.lazur.outlineVariantDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.outlineVariantDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.outlineVariantLight
import org.turter.patrocl.ui.theme.lazur.outlineVariantLightHighContrast
import org.turter.patrocl.ui.theme.lazur.outlineVariantLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.primaryContainerDark
import org.turter.patrocl.ui.theme.lazur.primaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.primaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.primaryContainerLight
import org.turter.patrocl.ui.theme.lazur.primaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.primaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.primaryDark
import org.turter.patrocl.ui.theme.lazur.primaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.primaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.primaryLight
import org.turter.patrocl.ui.theme.lazur.primaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.primaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.scrimDark
import org.turter.patrocl.ui.theme.lazur.scrimDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.scrimDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.scrimLight
import org.turter.patrocl.ui.theme.lazur.scrimLightHighContrast
import org.turter.patrocl.ui.theme.lazur.scrimLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.secondaryContainerDark
import org.turter.patrocl.ui.theme.lazur.secondaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.secondaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.secondaryContainerLight
import org.turter.patrocl.ui.theme.lazur.secondaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.secondaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.secondaryDark
import org.turter.patrocl.ui.theme.lazur.secondaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.secondaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.secondaryLight
import org.turter.patrocl.ui.theme.lazur.secondaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.secondaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceBrightDark
import org.turter.patrocl.ui.theme.lazur.surfaceBrightDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceBrightDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceBrightLight
import org.turter.patrocl.ui.theme.lazur.surfaceBrightLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceBrightLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerDark
import org.turter.patrocl.ui.theme.lazur.surfaceContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighDark
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighLight
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestDark
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestLight
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerHighestLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLight
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowDark
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowLight
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestDark
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestLight
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceContainerLowestLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDark
import org.turter.patrocl.ui.theme.lazur.surfaceDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDimDark
import org.turter.patrocl.ui.theme.lazur.surfaceDimDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDimDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDimLight
import org.turter.patrocl.ui.theme.lazur.surfaceDimLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceDimLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceLight
import org.turter.patrocl.ui.theme.lazur.surfaceLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceVariantDark
import org.turter.patrocl.ui.theme.lazur.surfaceVariantDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceVariantDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.surfaceVariantLight
import org.turter.patrocl.ui.theme.lazur.surfaceVariantLightHighContrast
import org.turter.patrocl.ui.theme.lazur.surfaceVariantLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerDark
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerLight
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerLightHighContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryContainerLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryDark
import org.turter.patrocl.ui.theme.lazur.tertiaryDarkHighContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryDarkMediumContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryLight
import org.turter.patrocl.ui.theme.lazur.tertiaryLightHighContrast
import org.turter.patrocl.ui.theme.lazur.tertiaryLightMediumContrast
import org.turter.patrocl.ui.theme.lazur.AppTypography

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      darkTheme -> mediumContrastDarkColorScheme
      else -> mediumContrastLightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

