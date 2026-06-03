package ru.course.recipemanager.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.control.RecipeUiState
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.presentation.components.RecipeListCard
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Panel

@Composable
fun CatalogScreen(
    state: RecipeUiState,
    onSearch: (String) -> Unit,
    onCategorySelected: (RecipeCategory?) -> Unit,
    onOpenRecipe: (Recipe) -> Unit,
    onAdd: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Каталог", color = Berry, fontWeight = FontWeight.ExtraBold)
                    Text("Что приготовить?", style = MaterialTheme.typography.headlineLarge)
                }
                Button(
                    onClick = onAdd,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Добавить рецепт")
                }
            }
        }

        item {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearch,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Поиск") },
                placeholder = { Text("каша, суп, паста...") },
                shape = RoundedCornerShape(18.dp),
                singleLine = true
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.selectedCategory == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("Все") }
                )
                RecipeCategory.entries.take(3).forEach { category ->
                    FilterChip(
                        selected = state.selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category.label) }
                    )
                }
            }
        }

        if (state.filteredRecipes.isEmpty()) {
            item {
                Text(
                    text = "Ничего не найдено. Можно добавить свой рецепт.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAdd() }
                        .padding(20.dp)
                )
            }
        } else {
            items(state.filteredRecipes) { recipe ->
                RecipeListCard(recipe = recipe, onClick = { onOpenRecipe(recipe) })
            }
        }
    }
}
