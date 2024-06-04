package com.lollipop.whs.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.lollipop.whs.R
import com.lollipop.whs.databinding.ActivityMainBinding
import com.lollipop.whs.service.ProxyService
import com.lollipop.whs.utils.ProxyBroadcast
import com.lollipop.whs.utils.ProxyConfig

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vpnPermissionLauncher by lazy {
        ProxyConfig.registerPermissionResult(this) {
            if (it) {
                allowConnections()
            }
        }
    }

    private var receiver: ProxyBroadcast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.actionBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.connectButton) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val dp16 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                resources.displayMetrics
            ).toInt()
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = systemBars.left + dp16
                bottomMargin = systemBars.bottom + dp16
                rightMargin = systemBars.right + dp16
            }
            insets
        }

        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.connectButton.setOnClickListener {
            onConnectButtonClick()
        }
        // init
        vpnPermissionLauncher
    }

    override fun onStart() {
        super.onStart()
        receiver = ProxyBroadcast.register(this) {
            onConnectStateChanged(it)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun onConnectButtonClick() {
        if (ProxyConfig.isConnected) {
            disconnect()
        } else {
            connect()
        }
    }

    private fun onConnectStateChanged(action: ProxyBroadcast.Action) {
        updateConnectButton()
    }

    private fun updateConnectButton() {
        binding.connectButton.setIconResource(
            if (ProxyConfig.isConnected) {
                R.drawable.ic_link_off_24dp
            } else {
                R.drawable.ic_link_24dp
            }
        )
        binding.connectButton.setText(
            if (ProxyConfig.isConnected) {
                R.string.disconnect
            } else {
                R.string.connect
            }
        )
    }

    private fun connect() {
        val intent = ProxyConfig.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            allowConnections()
        }
    }

    private fun disconnect() {
        ProxyBroadcast.cutOff(this)
    }

    private fun allowConnections() {
        // TODO
        startService(Intent(this, ProxyService::class.java))
    }

}