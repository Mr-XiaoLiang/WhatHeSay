package com.lollipop.whs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

object ProxyConfig {

    var isConnected = false

    fun prepare(context: Context): Intent? {
        return VpnService.prepare(context)
    }

    fun registerPermissionResult(
        activity: ComponentActivity,
        callback: ActivityResultCallback<Boolean>
    ): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(VpnServiceContract(), callback)
    }

    private class VpnServiceContract : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }

    }

}