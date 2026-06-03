package ru.course.recipemanager.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.course.recipemanager.control.RecipeViewModel
import ru.course.recipemanager.presentation.components.LadushkiBottomBar
import ru.course.recipemanager.presentation.screens.AddRecipeScreen
import ru.course.recipemanager.presentation.screens.AdminScreen
import ru.course.recipemanager.presentation.screens.AuthScreen
import ru.course.recipemanager.presentation.screens.CatalogScreen
import ru.course.recipemanager.presentation.screens.HomeScreen
import ru.course.recipemanager.presentation.screens.ProfileScreen
import ru.course.recipemanager.presentation.screens.RecipeDetailScreen
import ru.course.recipemanager.presentation.screens.SettingsScreen
import ru.course.recipemanager.presentation.screens.ShoppingListScreen
import ru.course.recipemanager.ui.theme.Milk

enum class AppRoute(val route: String, val label: String) {
    Auth("auth", "Вход"),
    Home("home", "Домой"),
    Catalog("catalog", "Поиск"),
    Add("add", "Добавить"),
    Shopping("shopping", "Покупки"),
    Profile("profile", "Профиль"),
    Detail("detail", "Рецепт"),
    Edit("edit", "Редактировать"),
    Settings("settings", "Настройки"),
    Admin("admin", "Админ")
}

@Composable
fun LadushkiApp(viewModel: RecipeViewModel = viewModel()) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: AppRoute.Home.route

    LaunchedEffect(state.isAuthenticated, currentRoute) {
        if (state.isAuthenticated && currentRoute == AppRoute.Auth.route) {
            navController.navigate(AppRoute.Home.route) {
                popUpTo(AppRoute.Auth.route) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (state.isAuthenticated) {
                LadushkiBottomBar(
                    currentRoute = currentRoute,
                    onOpen = { route ->
                        navController.navigate(route.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .background(Milk)
                .padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = AppRoute.Auth.route
            ) {
                composable(AppRoute.Auth.route) {
                    AuthScreen(
                        message = state.message,
                        onLogin = viewModel::login,
                        onRegister = viewModel::register,
                        onMessageShown = viewModel::clearMessage
                    )
                }
                composable(AppRoute.Home.route) {
                    HomeScreen(
                        state = state,
                        onOpenCatalog = { navController.navigate(AppRoute.Catalog.route) },
                        onOpenCategory = { category ->
                            viewModel.selectCategory(category)
                            navController.navigate(AppRoute.Catalog.route)
                        },
                        onOpenDetail = { navController.navigate(AppRoute.Detail.route) },
                        onOpenProfile = { navController.navigate(AppRoute.Profile.route) }
                    )
                }
                composable(AppRoute.Catalog.route) {
                    CatalogScreen(
                        state = state,
                        onSearch = viewModel::updateSearch,
                        onCategorySelected = viewModel::selectCategory,
                        onOpenRecipe = {
                            viewModel.selectRecipe(it)
                            navController.navigate(AppRoute.Detail.route)
                        },
                        onAdd = { navController.navigate(AppRoute.Add.route) }
                    )
                }
                composable(AppRoute.Detail.route) {
                    RecipeDetailScreen(
                        recipe = state.selectedRecipe,
                        message = state.message,
                        onBack = { navController.popBackStack() },
                        onAddShopping = viewModel::addSelectedRecipeToShoppingList,
                        onToggleFavorite = { recipe -> viewModel.toggleFavorite(recipe.id) },
                        onEdit = { navController.navigate(AppRoute.Edit.route) },
                        onMessageShown = viewModel::clearMessage
                    )
                }
                composable(AppRoute.Add.route) {
                    AddRecipeScreen(
                        editableRecipe = null,
                        onSave = { title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri ->
                            viewModel.saveRecipe(title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                            navController.navigate(AppRoute.Catalog.route) {
                                popUpTo(AppRoute.Home.route)
                            }
                        },
                        onUpdate = { id, title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri ->
                            viewModel.updateRecipe(id, title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                            navController.navigate(AppRoute.Detail.route)
                        }
                    )
                }
                composable(AppRoute.Edit.route) {
                    AddRecipeScreen(
                        editableRecipe = state.selectedRecipe,
                        onSave = { title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri ->
                            viewModel.saveRecipe(title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                            navController.navigate(AppRoute.Catalog.route)
                        },
                        onUpdate = { id, title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri ->
                            viewModel.updateRecipe(id, title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                            navController.navigate(AppRoute.Detail.route) {
                                popUpTo(AppRoute.Detail.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(AppRoute.Shopping.route) {
                    ShoppingListScreen(
                        items = state.shoppingItems,
                        onToggle = viewModel::toggleShoppingItem,
                        onClearCompleted = viewModel::clearCompletedShoppingItems,
                        onClearAll = viewModel::clearShoppingList
                    )
                }
                composable(AppRoute.Profile.route) {
                    ProfileScreen(
                        profile = state.profile,
                        onOpenSettings = { navController.navigate(AppRoute.Settings.route) },
                        onOpenAdmin = { navController.navigate(AppRoute.Admin.route) },
                        onUpdateProfile = viewModel::updateProfile,
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(AppRoute.Auth.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
                composable(AppRoute.Settings.route) {
                    SettingsScreen(
                        settings = state.settings,
                        onBack = { navController.popBackStack() },
                        onSave = viewModel::saveSettings
                    )
                }
                composable(AppRoute.Admin.route) {
                    AdminScreen(
                        stats = state.adminStats,
                        recipes = state.recipes,
                        pendingRecipes = state.recipes.filter { recipe -> recipe.id in state.pendingRecipeIds },
                        onBack = { navController.popBackStack() },
                        onApproveRecipe = viewModel::approveRecipe,
                        onDeleteRecipe = viewModel::deleteRecipe
                    )
                }
            }
        }
    }
}
