package com.example.alcholrehab

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.alcholrehab.utils.ForceUpgradeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AlcoholRehab : Application(), LifecycleObserver {

    private var forceUpgradeManager : ForceUpgradeManager = ForceUpgradeManager()

    companion object{
        lateinit var application: AlcoholRehab
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initForceUpgradeManager()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initForceUpgradeManager() {
        forceUpgradeManager.forceUpgradeManager(application)
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

}