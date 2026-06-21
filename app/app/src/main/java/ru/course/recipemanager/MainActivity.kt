package ru.course.recipemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.course.recipemanager.presentation.LadushkiApp
import ru.course.recipemanager.ui.theme.LadushkiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LadushkiTheme {
                LadushkiApp()
            }
        }
    }
}
