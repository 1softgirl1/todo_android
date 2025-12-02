package com.example.tasks.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.SortType
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: TasksRepository
) : ViewModel() {

    /** Текущая выбранная дата */
    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    /** Текущий тип сортировки */
    val sortType: StateFlow<SortType> = repository.sortType
        .stateIn(viewModelScope, SharingStarted.Lazily, SortType.DATE_ASC)

    /** Список задач на выбранную дату */
    val tasks: StateFlow<List<Task>> = _selectedDate
        .flatMapLatest { date: Long ->
            val startOfDay = Calendar.getInstance().apply {
                timeInMillis = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDay = Calendar.getInstance().apply {
                timeInMillis = startOfDay
                add(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis

            repository.getTasksByDateRange(startOfDay, endOfDay)
        }
        .combine(sortType) { tasks: List<Task>, sort: SortType ->
            sortTasks(tasks, sort)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** Выбор даты через календарь или кнопки вперед/назад */
    fun selectDate(date: Long) {
        _selectedDate.value = date
        saveLastSelectedDate(date)
    }

    /** Сохранение последней выбранной даты в DataStore */
    private fun saveLastSelectedDate(date: Long) {
        viewModelScope.launch {
            repository.saveLastSelectedDate(date)
        }
    }

    /** Изменение сортировки */
    fun changeSort(sort: SortType) {
        viewModelScope.launch {
            repository.saveSortType(sort)
        }
    }

    /** Удаление задачи */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    /** Локальная сортировка списка задач */
    private fun sortTasks(tasks: List<Task>, sort: SortType): List<Task> {
        return when (sort) {
            SortType.DATE_ASC -> tasks.sortedBy { it.time }
            SortType.DATE_DESC -> tasks.sortedByDescending { it.time }
            SortType.TITLE_ASC -> tasks.sortedBy { it.title.lowercase(Locale.getDefault()) }
            SortType.TITLE_DESC -> tasks.sortedByDescending { it.title.lowercase(Locale.getDefault()) }
        }
    }
}
