/*
 * @created: 11 May 2021.
 * @author: Weiyi Li
 * Copyright (c) 2021 EROAD Limited. All rights reserved.
 */
package me.li2.android.common.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_FASTEST
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Create a coroutine [Flow] which emits the [android.hardware.SensorEvent] when there is a new sensor event.
 *
 * @param type of sensors requested
 */
@ExperimentalCoroutinesApi
fun Context.sensorChangesFlow(type: Int): Flow<SensorEvent> = callbackFlow {
    val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) sendBlocking(event)
        }
    }

    sensorManager.registerListener(
        sensorEventListener,
        sensorManager.getDefaultSensor(type),
        SENSOR_DELAY_FASTEST
    )
    awaitClose {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
