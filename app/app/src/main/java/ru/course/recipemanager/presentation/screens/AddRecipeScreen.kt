package ru.course.recipemanager.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.course.recipemanager.R
import ru.course.recipemanager.entity.Ingredient
import ru.course.recipemanager.entity.Recipe
import ru.course.recipemanager.ui.theme.Berry
import ru.course.recipemanager.ui.theme.Milk
import ru.course.recipemanager.ui.theme.Muted
import ru.course.recipemanager.ui.theme.Panel

@Composable
fun AddRecipeScreen(
    editableRecipe: Recipe? = null,
    onSave: (String, String, String, String, String, String, String, String, String, String, String?) -> Unit,
    onUpdate: (Long, String, String, String, String, String, String, String, String, String, String, String?) -> Unit
) {
    var title by remember(editableRecipe) { mutableStateOf(editableRecipe?.title ?: "Сырники с ягодами") }
    var category by remember(editableRecipe) { mutableStateOf(editableRecipe?.category?.label ?: "Завтрак") }
    var time by remember(editableRecipe) { mutableStateOf("${editableRecipe?.cookingTimeMinutes ?: 25} мин") }
    var portions by remember(editableRecipe) { mutableStateOf("${editableRecipe?.portions ?: 3}") }
    val ingredientRows = remember(editableRecipe?.id) {
        mutableStateListOf<IngredientDraft>().apply {
            addAll(
                editableRecipe?.ingredients?.map { it.toDraft() }
                    ?: listOf(
                        IngredientDraft("Творог", "400", "г"),
                        IngredientDraft("Яйцо", "1", "шт"),
                        IngredientDraft("Мука", "3", "ст. л."),
                        IngredientDraft("Ягоды", "120", "г"),
                        IngredientDraft("Мед", "1", "ч. л.")
                    )
            )
        }
    }
    var note by remember(editableRecipe) {
        mutableStateOf(editableRecipe?.description ?: "Подавать теплыми, сверху добавить ягоды.")
    }
    var calories by remember(editableRecipe) { mutableStateOf("${editableRecipe?.nutrition?.calories ?: 410}") }
    var protein by remember(editableRecipe) { mutableStateOf("${editableRecipe?.nutrition?.protein ?: 29}") }
    var fat by remember(editableRecipe) { mutableStateOf("${editableRecipe?.nutrition?.fat ?: 16}") }
    var carbs by remember(editableRecipe) { mutableStateOf("${editableRecipe?.nutrition?.carbs ?: 35}") }
    var imageUri by remember(editableRecipe) { mutableStateOf(editableRecipe?.imageUri) }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = uri.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Milk)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(if (editableRecipe == null) "Новый рецепт" else "Редактирование", color = Berry, fontWeight = FontWeight.ExtraBold)
        Text(if (editableRecipe == null) "Записать вкусное" else "Обновить рецепт", style = MaterialTheme.typography.headlineLarge)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Panel)
        ) {
            if (!imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Фото рецепта",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.berry_oatmeal),
                    contentDescription = "Фото рецепта",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Button(
            onClick = { imagePicker.launch(arrayOf("image/*")) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
        ) {
            Text(if (imageUri == null) "Выбрать фото из телефона" else "Заменить фото", fontWeight = FontWeight.ExtraBold)
        }

        WarmField("Название", title, { title = it })
        WarmField("Категория", category, { category = it })
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WarmField("Время", time, { time = it }, Modifier.weight(1f))
            WarmField("Порции", portions, { portions = it }, Modifier.weight(1f))
        }
        Text("Ингредиенты", color = Muted, fontWeight = FontWeight.ExtraBold)
        ingredientRows.forEachIndexed { index, ingredient ->
            IngredientEditorCard(
                ingredient = ingredient,
                onChange = { updated -> ingredientRows[index] = updated },
                onRemove = {
                    if (ingredientRows.size > 1) {
                        ingredientRows.removeAt(index)
                    }
                }
            )
        }
        Button(
            onClick = { ingredientRows += IngredientDraft("", "", "г") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Panel, contentColor = Berry)
        ) {
            Text("Добавить ингредиент", fontWeight = FontWeight.ExtraBold)
        }
        WarmField("Заметка", note, { note = it }, minLines = 4)
        Text("КБЖУ на порцию", color = Muted, fontWeight = FontWeight.ExtraBold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WarmField("Ккал", calories, { calories = it }, Modifier.weight(1f))
            WarmField("Белки", protein, { protein = it }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WarmField("Жиры", fat, { fat = it }, Modifier.weight(1f))
            WarmField("Углеводы", carbs, { carbs = it }, Modifier.weight(1f))
        }

        Button(
            onClick = {
                val recipe = editableRecipe
                val ingredients = ingredientRows.serialize()
                if (recipe == null) {
                    onSave(title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                } else {
                    onUpdate(recipe.id, title, category, time, portions, ingredients, note, calories, protein, fat, carbs, imageUri)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Berry)
        ) {
            Text(if (editableRecipe == null) "Сохранить рецепт" else "Сохранить изменения", fontWeight = FontWeight.ExtraBold)
        }
    }
}

private data class IngredientDraft(
    val name: String,
    val amount: String,
    val unit: String
)

private fun Ingredient.toDraft(): IngredientDraft = IngredientDraft(name, amount, unit)

private fun List<IngredientDraft>.serialize(): String =
    filter { it.name.isNotBlank() }
        .joinToString("\n") { "${it.name.trim()}|${it.amount.trim()}|${it.unit.trim()}" }

@Composable
private fun IngredientEditorCard(
    ingredient: IngredientDraft,
    onChange: (IngredientDraft) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WarmField(
                label = "Название",
                value = ingredient.name,
                onValueChange = { onChange(ingredient.copy(name = it)) }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                WarmField(
                    label = "Кол-во",
                    value = ingredient.amount,
                    onValueChange = { onChange(ingredient.copy(amount = it)) },
                    modifier = Modifier.weight(1f)
                )
                WarmField(
                    label = "Ед.",
                    value = ingredient.unit,
                    onValueChange = { onChange(ingredient.copy(unit = it)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Milk, contentColor = Berry)
            ) {
                Text("Убрать ингредиент")
            }
        }
    }
}

@Composable
private fun WarmField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        shape = RoundedCornerShape(18.dp),
        minLines = minLines
    )
}
