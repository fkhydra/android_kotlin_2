package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.*
import android.os.Environment
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import android.widget.TextView
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.system.exitProcess

public var alt_ertek:Float = 0.0F
public var szoveg:String = ""
public var sdf = SimpleDateFormat("hh_mm_ss")
public var looper_szamlalo: Long = 0
public var celertek: Int = 5000
public var fajlnev:String = sdf.format(Date()) + ".txt"
public var idoertek_start:Calendar = Calendar.getInstance()
public var idoertek_end:Calendar = Calendar.getInstance()
public var FirstRun:Boolean = true
public var myTxt:TextView

public var magSensor: Sensor? = null
public var magFigyelo: SensorEventListener? = null
public var SzenzorManager: SensorManager? = null
/**/

class MainActivity : AppCompatActivity(),ActivityCompat.OnRequestPermissionsResultCallback {

private var mylistener:LocationListener? = null
private var mymanager:LocationManager? = null

override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState)
 setContentView(R.layout.activity_main)

 ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION),123)

 if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
 ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),123)
 else
 {
 // Engedély megadva!
 get_gpsdata()
 }

 idokoz?.setOnSeekBarChangeListener(object :
 SeekBar.OnSeekBarChangeListener {
 @SuppressLint("MissingPermission")
 override fun onProgressChanged(seek: SeekBar,
progress: Int, fromUser: Boolean) {
  celertek = (progress + 1 ) * 5 * 1000
  findViewById<TextView>(R.id.masodperckijelzo).text = ((progress+1)*5).toString() + " másodperc"
 }

 override fun onStartTrackingTouch(seek: SeekBar) {;}

 override fun onStopTrackingTouch(seek: SeekBar) {;}
 })

 findViewById<Button>(R.id.btnUjraindit).setOnClickListener {
 fajlnev = sdf.format(Date()) + ".txt"
 }

 findViewById<Button>(R.id.btnKilepes).setOnClickListener {
 exitProcess(0)
 }
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
 if (requestCode == 123 && grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
 {
 if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
 {
  val kulsofajl = File(this.getExternalFilesDir(null), fajlnev)
  kulsofajl.writeText("LONG,LAT,HEIGHT,ACC,SPD\n")
 }
 get_gpsdata()

 }
 else
 findViewById<TextView>(R.id.infotext).text = "Nincs engedély!"
}

@SuppressLint("MissingPermission")
public fun get_gpsdata()
{
 //IRANYTU******************
 SzenzorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
 magSensor = SzenzorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
 if (magSensor != null)
 {
 magFigyelo = object:SensorEventListener{
  override fun onSensorChanged(event: SensorEvent?) {
  ;
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {;}
 }
 }
 SzenzorManager?.registerListener(magFigyelo, magSensor,SensorManager.SENSOR_DELAY_NORMAL)

 //GPS beállítása
 var myszoveg:String =""
 var irany:Float = 0.0F
 mymanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

 mylistener = object:LocationListener {
 override fun onLocationChanged(location: Location?) {
  if (location != null)
  {
  idoertek_end = Calendar.getInstance()
  looper_szamlalo = idoertek_end.timeInMillis - idoertek_start.timeInMillis
  if(looper_szamlalo >= celertek )
  {
   alt_ertek = location?.longitude!!.toFloat()
   szoveg = "X:"+alt_ertek.toString()
   alt_ertek = location?.latitude!!.toFloat()
   szoveg += "\nY: "+alt_ertek.toString()
   alt_ertek = location?.altitude!!.toFloat()
   szoveg += "\nMag.: %d".format(alt_ertek.toInt())+" m"
   alt_ertek = location?.accuracy!!.toFloat()
   szoveg += "\nPont.: %d".format(alt_ertek.toInt())+" m"
   alt_ertek = round(location?.speed!!.toFloat() * 3.6F)
   szoveg += "\nSeb.: %d".format(alt_ertek.toInt())+" km/h"
   szoveg += "\nLogfájl: "+fajlnev
   findViewById<TextView>(R.id.infotext).text = szoveg

   myTxt = findViewById(R.id.iranyszoveg)
   irany = location?.bearing!!.toFloat()
   if(irany > 350 || irany < 10) myTxt.text = "É"
   else if(irany > 10 && irany < 80) myTxt.text = "ÉK"
   else if(irany > 80 && irany < 100) myTxt.text = "K"
   else if(irany > 100 && irany < 170) myTxt.text = "DK"
   else if(irany > 170 && irany < 190) myTxt.text = "D"
   else if(irany > 190 && irany < 260) myTxt.text = "DNY"
   else if(irany > 260 && irany < 280) myTxt.text = "NY"
   else if(irany > 280 && irany < 350) myTxt.text = "ÉNY"

   myszoveg = location?.longitude!!.toFloat().toString()+","+
location?.latitude!!.toFloat().toString()+","+
location?.altitude!!.toFloat().toString()+","+
location?.accuracy!!.toFloat().toString()+","+
location?.speed!!.toFloat().toString()+"\n"
   saveGPStoCSV(myszoveg)
   idoertek_start = idoertek_end

  }
  }
 }
 override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {;}
 override fun onProviderEnabled(provider: String?) {;}
 override fun onProviderDisabled(provider: String?) {;}
 }
 mymanager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0F,mylistener)

}

public fun saveGPStoCSV(texttosave:String)
{
 val kulsofajl = File(this.getExternalFilesDir(null), fajlnev)
 kulsofajl.appendText(texttosave)
}

}
