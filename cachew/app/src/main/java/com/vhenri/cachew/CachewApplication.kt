package com.vhenri.cachew

import android.app.Application
import com.vhenri.cachew.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CachewApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CachewApplication)
            modules(appModules)
        }
    }
}

val appModules = module {
    viewModel { MainViewModel() }
}