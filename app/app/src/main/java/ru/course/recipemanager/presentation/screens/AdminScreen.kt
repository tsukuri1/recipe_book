package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.entity.AdminStats
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel
import ru.course.recipemanager.ui.theme.Rose

@Composable
fun AdminScreen(
    stats: AdminStats,
    recipes: List<Recipe>,
    pendingRecipes: List<Recipe>,
    onBack: () -> Unit,
    onApproveRecipe: (Long) -> Unit,
    onDeleteRecipe: (Long) -> Unit
) {
    val complaints = remember {
        listOf(
            ComplaintUi(1, "Некорректный ингредиент", "Ягодная овсянка", "Пользователь пишет, что в рецепте не хватает количества молока.", "Новая"),
            ComplaintUi(2, "Дубликат рецепта", "Сырники к чаю", "Похожий рецепт уже опубликован другим автором.", "На проверке")
        )
    }
    val complaintStatuses = remember { mutableStateMapOf<Long, String>() }
    val selectedComplaintId = remember { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            androidx.compose.foundation.layout.Spacer(Modifier.height(14.dp))
            Button(
                onClick = onBack,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
            ) {
                Text("Назад")
            }
        }
        item {
            Text("Админ-панель", color = Berry, fontWeight = FontWeight.ExtraBold)
            Text("Контроль рецептов", style = MaterialTheme.typography.headlineLarge)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStat("${stats.usersCount}", "пользователей", Modifier.weight(1f))
                AdminStat("${stats.recipesCount}", "рецептов", Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStat("${stats.pendingRecipesCount}", "на проверке", Modifier.weight(1f))
                AdminStat("${stats.reportsCount}", "жалоба", Modifier.weight(1f))
            }
        }
        item {
            Text("Жалобы пользователей", style = MaterialTheme.typography.titleMedium)
        }
        items(complaints, key = { "complaint-${it.id}" }) { complaint ->
            val expanded = selectedComplaintId.value == complaint.id
            val status = complaintStatuses[complaint.id] ?: complaint.status
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.clickable {
                    selectedComplaintId.value = if (expanded) null else complaint.id
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(complaint.title, fontWeight = FontWeight.ExtraBold)
                            Text(complaint.recipeTitle, color = Muted)
                        }
                        Text(status, color = Berry, fontWeight = FontWeight.Bold)
                    }
                    if (expanded) {
                        Text(complaint.description, color = Muted)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { complaintStatuses[complaint.id] = "В работе" },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Rose, contentColor = Berry)
                            ) {
                                Text("В работу")
                            }
                            Button(
                                onClick = { complaintStatuses[complaint.id] = "Закрыта" },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Berry)
                            ) {
                                Text("Закрыть")
                            }
                        }
                    } else {
                        Text("Нажмите, чтобы посмотреть детали", color = Muted)
                    }
                }
            }
        }

        item {
            Text("Рецепты на проверке", style = MaterialTheme.typography.titleMedium)
        }
        if (pendingRecipes.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Panel)
                ) {
                    Text(
                        "Новых рецептов на проверку нет.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        color = Muted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        items(pendingRecipes, key = { "pending-${it.id}" }) { recipe ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(recipe.title, fontWeight = FontWeight.ExtraBold)
                    Text("Проверить описание, КБЖУ и фото перед публикацией.", color = Muted)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onApproveRecipe(recipe.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Berry)
                        ) {
                            Text("Одобрить")
                        }
                        Button(
                            onClick = { onDeleteRecipe(recipe.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Rose, contentColor = Berry)
                        ) {
                            Text("Отклонить")
                        }
                    }
                }
            }
        }

        item {
            Text("Все рецепты", style = MaterialTheme.typography.titleMedium)
        }
        items(recipes, key = { "recipe-${it.id}" }) { recipe ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipe.title, fontWeight = FontWeight.ExtraBold)
                        Text("${recipe.category.label} · ${recipe.cookingTimeMinutes} мин", color = Muted)
                    }
                    Button(
                        onClick = { onDeleteRecipe(recipe.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Rose, contentColor = Berry)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}

private data class ComplaintUi(
    val id: Long,
    val title: String,
    val recipeTitle: String,
    val description: String,
    val status: String
)

@Composable
private fun AdminStat(value: String, label: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, color = Berry, style = MaterialTheme.typography.headlineMedium)
            Text(label, color = Muted, fontWeight = FontWeight.Bold)
        }
    }
}
