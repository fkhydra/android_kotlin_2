package com.example.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

public var magSensor: Sensor? = null
public var magFigyelo: SensorEventListener? = null
public var SzenzorManager: SensorManager? = null
public var mytext:String = ""

class MainActivity : AppCompatActivity() {

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  accSensor = SzenzorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  magSensor = SzenzorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
  if (magSensor != null)
  {
   magFigyelo = object:SensorEventListener{
    override fun onSensorChanged(event: SensorEvent?) {
     if(event != null)
     {
      synchronized(this) {
       mytext = ""
       if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD)
       {
        mytext = mytext + "\nMagneticRaw: "+event.values.contentToString()
       }
       szoveg.text = mytext
      }
     }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {;}
   }
  }
  SzenzorManager?.registerListener(magFigyelo, magSensor,SensorManager.SENSOR_DELAY_FASTEST)
 }
}
