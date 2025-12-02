package com.example.tasks

import android.app.Application
import com.example.tasks.data.AppContainer

class TasksApplication : Application() {

    /**
     * AppContainer instance used by the rest of the classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
