package org.turter.patrocl.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Person_apron: ImageVector
    get() {
        if (_Person_apron != null) {
            return _Person_apron!!
        }
        _Person_apron = ImageVector.Builder(
            name = "Person_apron",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 480f)
                quadToRelative(-66f, 0f, -113f, -47f)
                reflectiveQuadToRelative(-47f, -113f)
                reflectiveQuadToRelative(47f, -113f)
                reflectiveQuadToRelative(113f, -47f)
                reflectiveQuadToRelative(113f, 47f)
                reflectiveQuadToRelative(47f, 113f)
                reflectiveQuadToRelative(-47f, 113f)
                reflectiveQuadToRelative(-113f, 47f)
                moveTo(160f, 800f)
                verticalLineToRelative(-112f)
                quadToRelative(0f, -34f, 17f, -62.5f)
                reflectiveQuadToRelative(47f, -43.5f)
                quadToRelative(60f, -30f, 124.5f, -46f)
                reflectiveQuadTo(480f, 520f)
                reflectiveQuadToRelative(131.5f, 16f)
                reflectiveQuadTo(736f, 582f)
                quadToRelative(30f, 15f, 47f, 43.5f)
                reflectiveQuadToRelative(17f, 62.5f)
                verticalLineToRelative(112f)
                close()
                moveToRelative(320f, -400f)
                quadToRelative(33f, 0f, 56.5f, -23.5f)
                reflectiveQuadTo(560f, 320f)
                reflectiveQuadToRelative(-23.5f, -56.5f)
                reflectiveQuadTo(480f, 240f)
                reflectiveQuadToRelative(-56.5f, 23.5f)
                reflectiveQuadTo(400f, 320f)
                reflectiveQuadToRelative(23.5f, 56.5f)
                reflectiveQuadTo(480f, 400f)
                moveToRelative(160f, 228f)
                verticalLineToRelative(92f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(-32f)
                quadToRelative(0f, -11f, -5f, -20f)
                reflectiveQuadToRelative(-15f, -14f)
                quadToRelative(-14f, -8f, -29.5f, -14.5f)
                reflectiveQuadTo(640f, 628f)
                moveToRelative(-240f, -21f)
                verticalLineToRelative(53f)
                horizontalLineToRelative(160f)
                verticalLineToRelative(-53f)
                quadToRelative(-20f, -4f, -40f, -5.5f)
                reflectiveQuadToRelative(-40f, -1.5f)
                reflectiveQuadToRelative(-40f, 1.5f)
                reflectiveQuadToRelative(-40f, 5.5f)
                moveTo(240f, 720f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(-92f)
                quadToRelative(-15f, 5f, -30.5f, 11.5f)
                reflectiveQuadTo(260f, 654f)
                quadToRelative(-10f, 5f, -15f, 14f)
                reflectiveQuadToRelative(-5f, 20f)
                close()
                moveToRelative(400f, 0f)
                horizontalLineTo(320f)
                close()
                moveTo(480f, 320f)
            }
        }.build()
        return _Person_apron!!
    }

private var _Person_apron: ImageVector? = null
