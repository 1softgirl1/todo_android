package com.example.tasks.ui.home

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tasks.data.SortType
import com.example.tasks.data.Task
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onTaskClick: (Int) -> Unit,
    onAddTaskClick: (Long) -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val sortType by viewModel.sortType.collectAsState()

    Scaffold(
        topBar = {
            HomeTopAppBar(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> viewModel.selectDate(newDate) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTaskClick(selectedDate) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                DateNavigation(
                    selectedDate = selectedDate,
                    onPrevious = { viewModel.selectDate(selectedDate - DAY_MS) },
                    onNext = { viewModel.selectDate(selectedDate + DAY_MS) }
                )
                SortMenu(currentSort = sortType, onSortSelected = { viewModel.changeSort(it) })
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onClick = { onTaskClick(task.id) },
                            onDelete = { viewModel.deleteTask(task) },
                            onEdit = { onTaskClick(task.id) } // Редирект на экран редактирования
                        )
                    }
                }
            }
        }
    )
}



@Composable
fun DateNavigation(selectedDate: Long, onPrevious: () -> Unit, onNext: () -> Unit) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateText = formatter.format(Date(selectedDate))

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPrevious) { Text("<") }
        Text(dateText, style = MaterialTheme.typography.titleLarge)

        Button(onClick = onNext) { Text(">") }
    }
}

@Composable
fun SortMenu(currentSort: SortType, onSortSelected: (SortType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Button(onClick = { expanded = true }) {
            Text("Sort: ${currentSort.name}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onSortSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(task.description, style = MaterialTheme.typography.titleSmall)
                Text("Time: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(task.time))}", style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать задачу")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить задачу")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current

    TopAppBar(
        title = { Text("Tasks") },
        actions = {
            IconButton(onClick = {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate

                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val newCalendar = Calendar.getInstance()
                        newCalendar.set(year, month, dayOfMonth)
                        onDateSelected(newCalendar.timeInMillis)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.show()
            }) {
                Icon(Icons.Filled.DateRange, contentDescription = "Выбрать дату")
            }
        }
    )
}

// ------------------- Константы -------------------
private const val DAY_MS = 24 * 60 * 60 * 1000L
