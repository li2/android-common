package me.li2.android.common.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_FASTEST
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Create an RxJava [Flowable] which emits the [android.hardware.SensorEvent] when there is a new sensor event.
 *
 * @param type of sensors requested
 */
fun Context.sensorChanges(type: Int): Flowable<SensorEvent> {
    return Flowable.create({ emitter ->
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) emitter.onNext(event)
            }
        }
        emitter.setDisposable(Disposable.fromRunnable {
            sensorManager.unregisterListener(sensorEventListener)
        })
        sensorManager.registerListener(
            sensorEventListener,
            sensorManager.getDefaultSensor(type),
            SENSOR_DELAY_FASTEST
        )
    }, BackpressureStrategy.BUFFER)
}
