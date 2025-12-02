package com.example.tasks.data

import android.content.Context

class AppContainer(context: Context) {

    private val database: TasksDatabase by lazy {
        TasksDatabase.getDatabase(context)
    }

    private val dataStore = PreferencesDataStore.getInstance(context)

    val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(
            taskDao = database.taskDao(),
            dataStore = dataStore
        )
    }
}
