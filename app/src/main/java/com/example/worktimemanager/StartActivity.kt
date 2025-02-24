package com.example.worktimemanager

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import com.example.worktimemanager.ui.theme.WorkTimeManagerTheme

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            val isDarkMode = remember { mutableStateOf(sharedPreferences.getBoolean("dark_mode", false)) }

            WorkTimeManagerTheme(darkTheme = isDarkMode.value) {
                StartScreen(
                    darkTheme = isDarkMode.value,
                    onThemeToggle = { isChecked ->
                        isDarkMode.value = isChecked  // ✅ Update state to trigger recomposition
                        sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
                        AppCompatDelegate.setDefaultNightMode(
                            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                            else AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun StartScreen(darkTheme: Boolean, onThemeToggle: (Boolean) -> Unit) {
    val context = LocalContext.current  // ✅ Now it's inside a @Composable function
    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // ✅ Ensures background follows theme
            .padding(16.dp)
    ) {
        Switch(
            checked = darkTheme,
            onCheckedChange = { onThemeToggle(it) }
        )

        Text(
            text = "Welcome to Work Time Manager",
            style = MaterialTheme.typography.headlineMedium,
            color = textColor
        )

        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)  // ✅ Start MainActivity correctly!
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start App")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen() {
    WorkTimeManagerTheme {
        StartScreen(darkTheme = false) { }
    }
}