package ru.course.recipemanager.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.presentation.AppRoute
import ru.course.recipemanager.ui.theme.BerryDark
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Rose

@Composable
fun LadushkiBottomBar(
    currentRoute: String,
    onOpen: (AppRoute) -> Unit
) {
    val routes = listOf(
        AppRoute.Home,
        AppRoute.Catalog,
        AppRoute.Add,
        AppRoute.Shopping,
        AppRoute.Profile
    )

    NavigationBar(
        containerColor = Rose,
        tonalElevation = 10.dp,
        modifier = Modifier.height(76.dp)
    ) {
        routes.forEach { route ->
            NavigationBarItem(
                selected = currentRoute == route.route,
                onClick = { onOpen(route) },
                icon = {
                    Icon(
                        imageVector = when (route) {
                            AppRoute.Home -> Icons.Rounded.Home
                            AppRoute.Catalog -> Icons.Rounded.Search
                            AppRoute.Add -> Icons.Rounded.Add
                            AppRoute.Shopping -> Icons.Rounded.ShoppingCart
                            else -> Icons.Rounded.Person
                        },
                        contentDescription = route.label
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BerryDark,
                    indicatorColor = Rose,
                    unselectedIconColor = Muted
                )
            )
        }
    }
}
