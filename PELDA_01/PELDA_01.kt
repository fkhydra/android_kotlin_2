package com.example.sensor_tests

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

public var SzenzorManager: SensorManager? = null
public var mytext:String = ""

class MainActivity : AppCompatActivity() {

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  SzenzorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) mytext += "\nACCELEROMETER YES"
  else mytext += "\nACCELEROMETER NO"
  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) mytext += "\nAMBIENT_TEMPERATURE YES"
  else mytext += "\nAMBIENT_TEMPERATURE NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) mytext += "\nGRAVITY YES"
  else mytext += "\nGRAVITY NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) mytext += "\nGYROSCOPE YES"
  else mytext += "\nGYROSCOPE NO"
  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_LIGHT) != null) mytext += "\nLIGHT YES"
  else mytext += "\nLIGHT NO"
  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) mytext += "\nLINEAR ACCELERATION YES"
  else mytext += "\nLINEAR ACCELERATION NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) mytext += "MAGNETIC YES"
  else mytext += "MAGNETIC NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) mytext += "\nPRESSURE YES"
  else mytext += "\nPRESSURE NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) mytext += "\nPROXIMITY YES"
  else mytext += "\nPROXIMITY NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) mytext += "\nRELATIVE_HUMIDITY YES"
  else mytext += "\nRELATIVE_HUMIDITY NO"

  if(SzenzorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) mytext += "\nROTATION_VECTOR YES"
  else mytext += "\nROTATION_VECTOR NO"

  szoveg.text = mytext
 }
}
