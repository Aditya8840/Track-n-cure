package com.nooglers.health.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.*


class BluetoothService : Service() {
    private val TAG = "BluetoothService"
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var timer: Timer

    override fun onCreate() {
        super.onCreate()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        timer = Timer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startScanning()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScanning()
    }

    private fun startScanning() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (bluetoothAdapter!!.isEnabled) {
                    val bluetoothDevices = bluetoothAdapter!!.bondedDevices
                    for (device in bluetoothDevices) {
                        val address = device.address
                        Log.d(TAG, "Found Bluetooth device with address: $address")
                    }
                } else {
                    Log.d(TAG, "Bluetooth is not enabled.")
                }
            }
        }, 0, 5000)
    }

    private fun stopScanning() {
        timer.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
