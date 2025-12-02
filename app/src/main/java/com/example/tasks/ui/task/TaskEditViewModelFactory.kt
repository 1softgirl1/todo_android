package com.example.tasks.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.data.TasksRepository

class TaskEditViewModelFactory(
    private val repository: TasksRepository,
    private val taskId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskEditViewModel(repository, taskId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

