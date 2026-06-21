package ru.course.recipemanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Berry,
    onPrimary = Color.White,
    secondary = Sage,
    onSecondary = Color.White,
    tertiary = Rose,
    background = Milk,
    onBackground = Ink,
    surface = Panel,
    onSurface = Ink,
    surfaceVariant = Rose,
    onSurfaceVariant = Muted,
    outline = Line
)

@Composable
fun LadushkiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = LadushkiTypography,
        content = content
    )
}
