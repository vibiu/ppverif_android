package com.example.ppverif

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

import android.widget.TextView
import android.widget.Button

import com.example.ppverif.RustBridge

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val myTextView = findViewById<TextView>(R.id.mText)
        myTextView.text = "Click to test!"
        val myButton = findViewById<Button>(R.id.mButton)
        myButton.setOnClickListener {
            val testResult: FloatArray = RustBridge.testClient()
            myTextView.text = "Encrypting query ct: " + testResult[0].toString() +  " micros.\n" +
                "Generate lookup table: " + testResult[1].toString() +  " micros.\n" +
                "Verification time: " + testResult[2].toString() +  " micros.\n" +
                "Decrytion time: " + testResult[3].toString() +  " micros.\n"
        }
    }
}