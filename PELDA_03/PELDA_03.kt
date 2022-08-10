package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText
import java.io.File

class MainActivity : AppCompatActivity() {

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  findViewById<Button>(R.id.button).setOnClickListener {
   if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
   {
    val kulsofajl = File(this.getExternalFilesDir(null), "myfile2.txt")
    kulsofajl.writeText(editText.text.toString())
   }
  }

  findViewById<Button>(R.id.button2).setOnClickListener {
   if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
   {
    val kulsofajl = File(this.getExternalFilesDir(null), "myfile2.txt")
    findViewById<EditText>(R.id.editText).setText(kulsofajl.readText())
   }
  }

 }
}
