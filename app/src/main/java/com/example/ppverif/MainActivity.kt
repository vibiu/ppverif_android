package com.example.ppverif

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

import android.widget.TextView
import android.widget.Button
import android.app.AlertDialog
import android.widget.ProgressBar

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.ppverif.RustBridge


class MainActivity : AppCompatActivity() {
    private var progressDialog: AlertDialog? = null

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

            myTextView.text = "Waiting for test..."
            myButton.isEnabled = false
            myButton.text = "Processing..."
            // coroutine
            lifecycleScope.launch {
                val repeatTime = 100
                val sampleNum = 6
                val testResult: FloatArray = withContext(Dispatchers.Default) {
                    RustBridge.testClient(repeatTime)
                }
                var resultContent = "Test repeats ${repeatTime} times.\n"
                resultContent += "***********************\n"
                val descriptions = arrayOf(
                    "DIMENSION_128",
                    "DIMENSION_256",
                    "DIMENSION_512",
                    "DIMENSION_1024",
                    "DIMENSION_2048",
                    "DIMENSION_4096"
                    )
                for (i in 0 until sampleNum) {
                    val offset = i * 5;
                    resultContent += "Result for ${descriptions[i]}\n"
                    resultContent += "Preprocessing time: ${testResult[offset + 0].toString()} micros.\n";
                    resultContent += "Encrypting query ct time: ${testResult[offset + 1].toString()} micros.\n";
                    resultContent += "Client innerproduct time: ${testResult[offset + 2].toString()} micros.\n";
                    resultContent += "Generate lookup table time: ${testResult[offset + 3].toString()} micros.\n";
                    resultContent += "Decrytion time: ${testResult[offset + 4].toString()} micros.\n";
                    resultContent += "-----------------------\n"
                }
                myTextView.text = resultContent
                myButton.isEnabled = true
                myButton.text = "TestClient Again"
            }

//            val repeatTime = 100
//            val sampleNum = 6
//            val testResult:FloatArray = RustBridge.testClient(repeatTime)
//            var resultContent = ""
//            for (i in 0..sampleNum) {
//                resultContent += "Preprocessing time: ${testResult[0].toString()} micros.\n";
//                resultContent += "Encrypting query ct time: ${testResult[0].toString()} micros.\n";
//                resultContent += "Client innerproduct time: ${testResult[0].toString()} micros.\n";
//                resultContent += "Generate lookup table time: ${testResult[0].toString()} micros.\n";
//                resultContent += "Decrytion time: ${testResult[0].toString()} micros.\n";
//                resultContent += "-------\n"
//            }
//            myTextView.text = resultContent

        }
    }

}