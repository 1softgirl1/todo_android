package com.example.tasks.data

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(
    private val taskDao: TaskDao,
    private val dataStore: PreferencesDataStore
) : TasksRepository {

    // ---------------- ROOM ----------------

    override fun getTasksByDate(date: Long): Flow<List<Task>> =
        taskDao.getTasksByDate(date)

    override suspend fun insertTask(task: Task) =
        taskDao.insert(task)

    override suspend fun updateTask(task: Task) =
        taskDao.update(task)

    override suspend fun deleteTask(task: Task) =
        taskDao.delete(task)

    override fun getTaskById(id: Int): Flow<Task?> =
        taskDao.getTaskById(id)

    // Добавленный метод для получения задач в диапазоне дат
    override fun getTasksByDateRange(startDate: Long, endDate: Long): Flow<List<Task>> =
        taskDao.getTasksByDateRange(startDate, endDate)

    // ---------------- DATASTORE ----------------

    override val sortType: Flow<SortType> =
        dataStore.sortTypeFlow

    override val lastSelectedDate: Flow<Long> =
        dataStore.lastDateFlow

    override suspend fun saveSortType(type: SortType) =
        dataStore.saveSortType(type)

    override suspend fun saveLastSelectedDate(timestamp: Long) =
        dataStore.saveLastDate(timestamp)
}
