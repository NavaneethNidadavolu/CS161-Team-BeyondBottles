package com.example.alcholrehab.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.alcholrehab.AlcoholRehab
import com.example.alcholrehab.R
import java.lang.ref.WeakReference


class ForceUpgradeManager : LifecycleObserver {

    private val keyUpdateRequired = "force_update_required"
    private val keyCurrentVersion = "force_update_current_version"

    private val TAG = "ForceUpgradeManager"
    private var context: Context? = null

    @Nullable
    private var activityWeakReference: WeakReference<Activity>? = null

    fun forceUpgradeManager(application: AlcoholRehab) {
        context = application.applicationContext
        application.registerActivityLifecycleCallbacks(callbacks)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun appStopped() {
        activityWeakReference?.clear()
    }

    private fun getCurrentActivity(): Activity? {
        return if (activityWeakReference != null && activityWeakReference!!.get() != null) activityWeakReference!!.get() else null
    }

    private val callbacks: ActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {
            activityWeakReference = WeakReference(activity)
        }

        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {}
    }


    private fun getAppVersion(context: Context): String? {
        var result = ""
        try {
            result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message!!)
        }
        return result
    }

    /**
     * Gets update alert.
     */
    private fun onUpdateNeeded() {
        val temp = getCurrentActivity()
        if (temp != null) {
            val dialog: AlertDialog = AlertDialog.Builder(temp, R.style.YourAlertDialogTheme)
                .setTitle("New version available")
                .setMessage("Please update app for seamless experience.")
                .setPositiveButton("Continue", { dialog, which -> redirectStore() })
                .setCancelable(false).create()
            dialog.show()
        }
    }

    //Redirect to play store
    private fun redirectStore() {
        val updateUrl: Uri = Uri.parse("https://play.google.com/store/apps/details?id=com.upscpre.iasprep")
        val intent = Intent(Intent.ACTION_VIEW, updateUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)
    }


}