package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.control.RecipeUiState
import ru.course.recipemanager.entity.RecipeCategory
import ru.course.recipemanager.presentation.components.FeaturedRecipeCard
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Ink
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel

@Composable
fun HomeScreen(
    state: RecipeUiState,
    onOpenCatalog: () -> Unit,
    onOpenCategory: (RecipeCategory?) -> Unit,
    onOpenDetail: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val categories: List<Pair<String, RecipeCategory?>> = listOf(
        "Завтраки" to RecipeCategory.Breakfast,
        "Ужины" to RecipeCategory.Dinner,
        "Десерты" to RecipeCategory.Dessert,
        "До 20 минут" to null
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Сегодня готовим спокойно", color = Berry, fontWeight = FontWeight.ExtraBold)
                Text("Ладушки", style = MaterialTheme.typography.headlineLarge)
                Text("Домашние рецепты, покупки и любимые блюда рядом.", color = Muted)
            }
            Button(
                onClick = onOpenProfile,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
            ) {
                Text("♡")
            }
        }

        FeaturedRecipeCard(
            recipe = state.selectedRecipe,
            onClick = onOpenDetail
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Быстрые подборки", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Все",
                color = Berry,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { onOpenCatalog() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(168.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { (title, category) ->
                Text(
                    text = title,
                    color = Ink,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(Panel)
                        .clickable { onOpenCategory(category) }
                        .padding(22.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
