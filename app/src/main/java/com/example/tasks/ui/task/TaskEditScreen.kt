package com.example.tasks.ui.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun TaskEditScreen(
    viewModel: TaskEditViewModel,
    selectedDate: Long,
    onTaskUpdated: () -> Unit
) {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    LaunchedEffect(selectedDate) {
        if (selectedDate > 0L) {
            calendar.timeInMillis = selectedDate
        }
    }

    var dateText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }

    fun updateDateTimeTexts() {
        val d = calendar.time
        val dateFmt = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFmt = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
        dateText = dateFmt.format(d)
        timeText = timeFmt.format(d)
    }

    LaunchedEffect(calendar.timeInMillis) {
        updateDateTimeTexts()
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateTimeTexts()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeTexts()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = { Text("Название задачи") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text("Описание задачи") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { datePickerDialog.show() }) {
                        Text(text = "Выбрать дату: $dateText")
                    }

                    Button(onClick = { timePickerDialog.show() }) {
                        Text(text = "Выбрать время: $timeText")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.updateTask(calendar.timeInMillis) {
                            onTaskUpdated()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Сохранить задачу")
                }
            }
        }

}
