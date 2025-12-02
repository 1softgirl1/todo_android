package com.example.tasks.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskEntryViewModel(
    private val repository: TasksRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _time = MutableStateFlow(System.currentTimeMillis())
    val time: StateFlow<Long> = _time

    fun onTitleChange(value: String) {
        _title.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun onTimeChange(value: Long) {
        _time.value = value
    }

    fun saveTask(selectedDate: Long, onComplete: () -> Unit) {
        val newTask = Task(
            title = _title.value,
            description = _description.value,
            date = selectedDate,
            time = _time.value
        )
        viewModelScope.launch {
            repository.insertTask(newTask)
            onComplete()
        }
    }
}
