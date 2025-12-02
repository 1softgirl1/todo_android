package com.example.tasks.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskEditViewModel(
    private val repository: TasksRepository,
    private val taskId: Int
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _time = MutableStateFlow(System.currentTimeMillis())
    val time: StateFlow<Long> = _time

    init {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                task?.let {
                    _title.value = it.title
                    _description.value = it.description
                    _time.value = it.time
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun updateTask(newTime: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply { timeInMillis = newTime }

            // Логирование для проверки времени
            println("Updating task with time: $newTime")

            val dateOnly = Calendar.getInstance().apply {
                set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val updatedTask = Task(
                id = taskId,
                title = _title.value,
                description = _description.value,
                date = dateOnly,
                time = newTime
            )

            repository.updateTask(updatedTask)
            _time.value = newTime
            onComplete()
        }
    }
}
