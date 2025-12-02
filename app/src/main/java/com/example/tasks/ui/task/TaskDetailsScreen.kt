package com.example.tasks.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel,
    onEditClick: () -> Unit,
    onTaskDeleted: () -> Unit
) {
    val task by viewModel.task.collectAsState()

    task?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Название: ${it.title}", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Описание: ${it.description}", style = MaterialTheme.typography.titleMedium )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Дата: ${SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it.date)}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Время: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.time)}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = onEditClick) {
                            Text("Редактировать")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { viewModel.deleteTask(onTaskDeleted) }) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Задача не найдена")
        }
    }
}
