package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.presentation.components.RecipeImage
import ru.course.recipemanager.presentation.components.WarmPill
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.BerryDark
import ru.course.recipemanager.ui.theme.Ink
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel
import ru.course.recipemanager.ui.theme.Rose
import ru.course.recipemanager.ui.theme.Sage

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    message: String?,
    onBack: () -> Unit,
    onAddShopping: () -> Unit,
    onToggleFavorite: (Recipe) -> Unit,
    onEdit: () -> Unit,
    onMessageShown: () -> Unit
) {
    if (recipe == null) return

    LaunchedEffect(message) {
        if (message != null) {
            kotlinx.coroutines.delay(1800)
            onMessageShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        androidx.compose.foundation.layout.Spacer(Modifier.height(14.dp))
        Button(
            onClick = onBack,
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
        ) {
            Text("Назад")
        }

        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            RecipeImage(
                recipe = recipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            )
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WarmPill(recipe.category.label)
                Text(recipe.title, style = MaterialTheme.typography.headlineLarge)
                Text(recipe.description, color = Muted)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Metric("${recipe.cookingTimeMinutes} мин")
                    Metric("${recipe.portions} порции")
                    Metric(recipe.difficulty.label)
                }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Rose)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("КБЖУ на порцию", fontWeight = FontWeight.ExtraBold, color = BerryDark)
                        Text(
                            "${recipe.nutrition.calories} ккал",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Ink
                        )
                        Text("Белки ${recipe.nutrition.protein} г · Жиры ${recipe.nutrition.fat} г · Углеводы ${recipe.nutrition.carbs} г", color = Muted)
                    }
                }

                Text("Ингредиенты", style = MaterialTheme.typography.titleMedium)
                recipe.ingredients.forEach {
                    Text("• ${it.displayName()}", color = Muted)
                }

                Text("Шаги", style = MaterialTheme.typography.titleMedium)
                recipe.steps.forEach {
                    Text("${it.number}. ${it.text}", color = Muted)
                }

                Button(
                    onClick = onAddShopping,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Berry)
                ) {
                    Text("Добавить в покупки", fontWeight = FontWeight.ExtraBold)
                }
                Button(
                    onClick = { onToggleFavorite(recipe) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (recipe.isFavorite) Rose else Sage,
                        contentColor = if (recipe.isFavorite) BerryDark else Panel
                    )
                ) {
                    Text(if (recipe.isFavorite) "Убрать из избранного" else "Добавить в избранное")
                }
                Button(
                    onClick = onEdit,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
                ) {
                    Text("Редактировать рецепт", fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        if (message != null) {
            Snackbar(containerColor = Sage) {
                Text(message)
            }
        }
    }
}

@Composable
private fun Metric(text: String) {
    Text(
        text = text,
        color = BerryDark,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(Rose, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    )
}
