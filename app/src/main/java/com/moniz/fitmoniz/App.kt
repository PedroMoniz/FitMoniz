package com.moniz.fitmoniz

import android.app.Application
import android.content.res.Configuration
import com.moniz.domain.usecases.exercises.SetupAppMemoryUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var setupAppMemoryUseCase: SetupAppMemoryUseCase

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(SupervisorJob()).launch {
            setupAppMemoryUseCase(Unit)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}
