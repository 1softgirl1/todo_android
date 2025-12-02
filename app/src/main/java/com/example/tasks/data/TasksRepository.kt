package com.example.tasks.data

import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    // ---------------- ROOM ----------------

    /** Получение задач на конкретную дату */
    fun getTasksByDate(date: Long): Flow<List<Task>>

    /** Получение одной задачи по ID */
    fun getTaskById(id: Int): Flow<Task?>

    /** Вставка новой задачи */
    suspend fun insertTask(task: Task)

    /** Обновление задачи */
    suspend fun updateTask(task: Task)

    /** Удаление задачи */
    suspend fun deleteTask(task: Task)

    /** Получение задач в диапазоне дат */
    fun getTasksByDateRange(startDate: Long, endDate: Long): Flow<List<Task>>

    // ---------------- DATASTORE ----------------

    /** Текущий выбранный тип сортировки */
    val sortType: Flow<SortType>

    /** Последняя выбранная дата */
    val lastSelectedDate: Flow<Long>

    /** Сохранение типа сортировки */
    suspend fun saveSortType(type: SortType)

    /** Сохранение выбранной даты */
    suspend fun saveLastSelectedDate(timestamp: Long)
}
