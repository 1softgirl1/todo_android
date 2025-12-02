package com.example.tasks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.tasks.ui.home.HomeViewModelFactory
import com.example.tasks.ui.task.TaskEntryViewModelFactory
import com.example.tasks.ui.task.TaskDetailsViewModelFactory
import com.example.tasks.ui.task.TaskEditViewModelFactory
import com.example.tasks.data.TasksRepository
import com.example.tasks.ui.home.HomeScreen
import com.example.tasks.ui.home.HomeViewModel
import com.example.tasks.ui.task.*

@Composable
fun TasksNavHost(
    repository: TasksRepository,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationDestination.HOME,
        modifier = modifier
    ) {
        // ---------------- HomeScreen ----------------
        composable(NavigationDestination.HOME) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))
            HomeScreen(
                viewModel = homeViewModel,
                onTaskClick = { taskId ->
                    navController.navigate("task_details/$taskId")
                },
                onAddTaskClick = { date ->
                    navController.navigate("task_entry/$date")
                }
            )
        }

        // ---------------- TaskEntryScreen ----------------
        composable(route = "task_entry/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")?.toLongOrNull() ?: System.currentTimeMillis()
            val taskEntryViewModel: TaskEntryViewModel =
                viewModel(factory = TaskEntryViewModelFactory(repository))
            TaskEntryScreen(
                viewModel = taskEntryViewModel,
                selectedDate = date,
                onTaskSaved = { navController.popBackStack() }
            )
        }

        // ---------------- TaskDetailsScreen ----------------
        composable(route = "task_details/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            val taskDetailsViewModel: TaskDetailsViewModel =
                viewModel(factory = TaskDetailsViewModelFactory(repository, taskId))
            TaskDetailsScreen(
                viewModel = taskDetailsViewModel,
                onEditClick = {
                    navController.navigate("task_edit/$taskId")
                },
                onTaskDeleted = {
                    navController.popBackStack(NavigationDestination.HOME, inclusive = false)
                }
            )
        }

        // ---------------- TaskEditScreen ----------------
        composable(route = "task_edit/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            val taskEditViewModel: TaskEditViewModel =
                viewModel(factory = TaskEditViewModelFactory(repository, taskId))

            val selectedDate = taskEditViewModel.time.collectAsState().value

            TaskEditScreen(
                viewModel = taskEditViewModel,
                selectedDate = selectedDate,
                onTaskUpdated = { navController.popBackStack() }
            )
        }

    }
}
