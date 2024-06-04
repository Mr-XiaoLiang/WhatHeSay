package com.lollipop.whs.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build

class ProxyBroadcast(
    private val callback: (Action) -> Unit
) : BroadcastReceiver() {

    companion object {

        fun cutOff(context: Context) {
            sendBroadcast(context, Action.CUT_OFF)
        }

        fun disconnected(context: Context) {
            sendBroadcast(context, Action.DISCONNECTED)
        }

        fun connecting(context: Context) {
            sendBroadcast(context, Action.CONNECTING)
        }

        private fun sendBroadcast(context: Context, action: Action) {
            context.sendBroadcast(Intent(action.value))
        }

        @SuppressLint("UnspecifiedRegisterReceiverFlag")
        fun register(context: Context, callback: (Action) -> Unit): ProxyBroadcast {
            val receiver = ProxyBroadcast(callback)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.registerReceiver(
                    receiver,
                    IntentFilter().apply {
                        addAction(Action.DISCONNECTED.value)
                        addAction(Action.CONNECTING.value)
                        addAction(Action.CUT_OFF.value)
                    },
                    Context.RECEIVER_NOT_EXPORTED
                )
            } else {
                context.registerReceiver(
                    receiver,
                    IntentFilter().apply {
                        addAction(Action.DISCONNECTED.value)
                        addAction(Action.CONNECTING.value)
                        addAction(Action.CUT_OFF.value)
                    }
                )
            }
            return receiver
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        when (action) {
            Action.DISCONNECTED.value -> {
                callback(Action.DISCONNECTED)
            }

            Action.CONNECTING.value -> {
                callback(Action.CONNECTING)
            }

            Action.CUT_OFF.value -> {
                callback(Action.CUT_OFF)
            }
        }
    }

    enum class Action(val value: String) {
        DISCONNECTED("com.lollipop.whs.ACTION_DISCONNECTED"),
        CONNECTING("com.lollipop.whs.ACTION_CONNECTING"),
        CUT_OFF("com.lollipop.whs.ACTION_CUT_OFF")
    }

}