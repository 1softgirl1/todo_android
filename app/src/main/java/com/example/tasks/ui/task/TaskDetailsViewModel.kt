package com.example.tasks.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    private val repository: TasksRepository,
    private val taskId: Int
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    init {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { _task.value = it }
        }
    }

    fun deleteTask(onComplete: () -> Unit) {
        viewModelScope.launch {
            _task.value?.let {
                repository.deleteTask(it)
                onComplete()
            }
        }
    }
}
