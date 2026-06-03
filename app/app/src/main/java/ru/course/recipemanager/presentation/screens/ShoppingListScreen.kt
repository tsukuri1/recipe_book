package ru.course.recipemanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ru.course.recipemanager.entity.ShoppingItem
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Mint
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel
import ru.course.recipemanager.ui.theme.Sage

@Composable
fun ShoppingListScreen(
    items: List<ShoppingItem>,
    onToggle: (Long) -> Unit,
    onClearCompleted: () -> Unit,
    onClearAll: () -> Unit
) {
    val boughtItems = items.filter { it.isChecked }
    val remainingItems = items.filterNot { it.isChecked }
    val progress = if (items.isEmpty()) 0f else boughtItems.size.toFloat() / items.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Список покупок", color = Berry, fontWeight = FontWeight.ExtraBold)
            Text("Покупаем без суеты", style = MaterialTheme.typography.headlineLarge)
        }

        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Mint)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Осталось ${remainingItems.size} · Куплено ${boughtItems.size}",
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = { progress },
                        color = Sage,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (boughtItems.isNotEmpty()) {
                        Button(
                            onClick = onClearCompleted,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Berry)
                        ) {
                            Text("Очистить купленное")
                        }
                    }
                    if (items.isNotEmpty()) {
                        Button(
                            onClick = onClearAll,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
                        ) {
                            Text("Очистить весь список", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            SectionTitle("Осталось купить", remainingItems.size)
        }
        if (remainingItems.isEmpty()) {
            item { EmptyShoppingCard("Все отмечено. Можно спокойно идти к кассе.") }
        } else {
            itemsIndexed(remainingItems, key = { index, item -> "todo-${item.id}-$index" }) { _, item ->
                ShoppingRow(item = item, onToggle = onToggle)
            }
        }

        item {
            SectionTitle("Уже куплено", boughtItems.size)
        }
        if (boughtItems.isEmpty()) {
            item { EmptyShoppingCard("Здесь появятся продукты, которые вы отметили галочкой.") }
        } else {
            itemsIndexed(boughtItems, key = { index, item -> "done-${item.id}-$index" }) { _, item ->
                ShoppingRow(item = item, onToggle = onToggle)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text("$count", color = Berry, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun ShoppingRow(item: ShoppingItem, onToggle: (Long) -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(item.id) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle(item.id) }
            )
            Text(
                text = item.name,
                color = if (item.isChecked) Muted else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@Composable
private fun EmptyShoppingCard(text: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            color = Muted,
            fontWeight = FontWeight.Bold
        )
    }
}
