package com.example.ppverif

object RustBridge {
    init {
        System.loadLibrary("ppverif_fhe")
    }

    @JvmStatic
    external fun testClient(): FloatArray
}