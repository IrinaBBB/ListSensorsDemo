package ru.irinavb.listsensorsdemo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.irinavb.listsensorsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager

    private var sensorProximity: Sensor? = null
    private var sensorLight: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val sensorError = resources.getString(R.string.error_no_sensor)

        if (sensorLight == null) {
            binding.labelLight.text = sensorError
        }

        if (sensorProximity == null) {
            binding.labelProximity.text = sensorError
        }
    }

    override fun onStart() {
        super.onStart()

        if(sensorProximity != null) {
            sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL)
        }

        if(sensorLight != null) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensorType = event?.sensor?.type
        val currentValue = event?.values?.get(0)
        when(sensorType) {
            Sensor.TYPE_LIGHT -> {
                binding.labelLight.text = resources.getString(R.string.label_light, currentValue)
            }
            Sensor.TYPE_PROXIMITY -> {
                binding.labelProximity.text = resources.getString(R.string.label_proximity, currentValue)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}