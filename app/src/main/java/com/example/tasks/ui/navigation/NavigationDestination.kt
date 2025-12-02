package com.example.tasks.ui.navigation

object NavigationDestination {
    const val HOME = "home"
    const val TASK_ENTRY = "task_entry/{date}"   // date - timestamp выбранной даты
    const val TASK_DETAILS = "task_details/{taskId}"  // taskId - ID задачи
    const val TASK_EDIT = "task_edit/{taskId}"   // taskId - ID задачи
}
