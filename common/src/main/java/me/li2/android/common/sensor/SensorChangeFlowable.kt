package me.li2.android.common.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_FASTEST
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber.d

/**
 * Create an flowable which emits the [SensorEvent] when sensor changes.
 */
fun Context.sensorChanges(): Flowable<SensorEvent> =
    Flowable.create(SensorChangesFlowable(this), BackpressureStrategy.BUFFER)

private class SensorChangesFlowable(context: Context)
    : FlowableOnSubscribe<SensorEvent>, Disposable {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensorEventListener: SensorEventListener?
    private lateinit var emitter: Emitter<in SensorEvent>

    init {
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (::emitter.isInitialized && event != null) {
                    emitter.onNext(event)
                }
            }
        }
    }

    override fun subscribe(emitter: FlowableEmitter<SensorEvent>) {
        d("register SensorEventListener")
        this.emitter = emitter
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SENSOR_DELAY_FASTEST)
    }

    override fun dispose() {
        d("unregister SensorEventListener")
        sensorEventListener?.let { sensorManager.unregisterListener(it) }
        sensorEventListener = null
    }

    override fun isDisposed(): Boolean = sensorEventListener != null
}
