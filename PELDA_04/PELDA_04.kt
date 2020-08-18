package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.*
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.*
import kotlin.system.exitProcess

public var elozo_long:Float = 0.0F
public var elozo_lat:Float = 0.0F
public var akt_long:Float = 0.0F
public var akt_lat:Float = 0.0F
public var tavolsag:Float = 0.0F
public var FirstRun:Boolean = true
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
   //Engedély megadva!
   get_gpsdata()
  }

  btnUjraindit.setOnClickListener {
   FirstRun = true
   tavolsag = 0.0F
  }

  btnKilepes.setOnClickListener {
   exitProcess(0)
  }
 }

 override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
  if (requestCode == 123 && grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
  {//GPS setup kész
   get_gpsdata()
  }
  else //Nincs engedély!
 }

 @SuppressLint("MissingPermission")
 public fun get_gpsdata()
 {
  var sebesseg: Float = 0.0F
  mymanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

  mylistener = object:LocationListener {
   override fun onLocationChanged(location: Location?) {
    if (location != null)
    {
     if(FirstRun == true)
     {
      FirstRun = false
      elozo_lat = location?.latitude!!.toFloat()
      elozo_long = location?.longitude!!.toFloat()
     }
     else
     {
      akt_long = location?.longitude!!.toFloat()
      akt_lat = location?.latitude!!.toFloat()
      sebesseg = 3.6F * (location?.speed!!.toFloat())
      tavolsag += ((acos(( cos((90.0F - elozo_lat) * (PI / 180.0F)) *
        cos((90.0F - akt_lat) * (PI/180.0F)) +
        sin((90.0F - elozo_lat) * (PI/180.0F)) *
        sin((90.0F - akt_lat) * (PI/180.0F)) *
        cos((elozo_long - akt_long) * (PI/180.0F)) ))).toFloat()) * 6371 * 1000
      elozo_lat = akt_lat
      elozo_long = akt_long
      if(tavolsag < 9999)
       infotext.text = "%d".format(sebesseg.toInt()) + "\n" + (tavolsag).toInt().toString()
      else
       infotext.text = "%d".format(sebesseg.toInt()) + "\n" + (tavolsag/1000).toInt().toString()
     }
    }
   }
   override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {;}
   override fun onProviderEnabled(provider: String?) {;}
   override fun onProviderDisabled(provider: String?) {;}
  }
  mymanager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,0F,mylistener)

 }
}
