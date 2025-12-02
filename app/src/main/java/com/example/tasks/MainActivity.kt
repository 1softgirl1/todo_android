package com.example.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tasks.ui.theme.TasksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val appContainer = (application as TasksApplication).container
        val repository = appContainer.tasksRepository

        setContent {
            TasksTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TasksApp(repository = repository)
                }
            }
        }
    }
}
