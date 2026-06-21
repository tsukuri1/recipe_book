package ru.course.recipemanager.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.course.recipemanager.R
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.ui.theme.BerryDark
import ru.course.recipemanager.ui.theme.Ink
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel
import ru.course.recipemanager.ui.theme.Rose

@Composable
fun FeaturedRecipeCard(
    recipe: Recipe?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            RecipeImage(
                recipe = recipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Panel.copy(alpha = 0.92f))
                    .padding(16.dp)
            ) {
                WarmPill("Рецепт дня")
                Spacer(Modifier.height(8.dp))
                Text(
                    text = recipe?.title ?: "Ягодная овсянка",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = recipe?.description ?: "Домашний рецепт для спокойной готовки.",
                    color = Muted
                )
                if (recipe != null) {
                    Text(recipe.nutrition.shortLabel(), color = Ink, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RecipeListCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecipeImage(
                recipe = recipe,
                modifier = Modifier
                    .size(98.dp)
                    .clip(RoundedCornerShape(19.dp))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(recipe.title, fontWeight = FontWeight.ExtraBold)
                Text(recipe.description, color = Muted, maxLines = 2)
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmallMeta(recipe.category.label)
                    SmallMeta("${recipe.cookingTimeMinutes} мин")
                    SmallMeta("${recipe.nutrition.calories} ккал")
                }
            }
        }
    }
}

@Composable
fun RecipeImage(
    recipe: Recipe?,
    modifier: Modifier = Modifier
) {
    if (!recipe?.imageUri.isNullOrBlank()) {
        AsyncImage(
            model = recipe?.imageUri,
            contentDescription = recipe?.title ?: "Фото рецепта",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(R.drawable.berry_oatmeal),
            contentDescription = recipe?.title ?: "Фото рецепта",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun WarmPill(text: String) {
    Text(
        text = text,
        color = Ink,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Rose)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    )
}

@Composable
private fun SmallMeta(text: String) {
    Text(
        text = text,
        color = BerryDark,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Rose)
            .padding(horizontal = 8.dp, vertical = 5.dp)
    )
}
