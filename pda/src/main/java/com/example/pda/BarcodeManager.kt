package com.example.pda

interface BarcodeSdkManager {
    fun init()
    fun addListener(listener: BarcodeSdkListener)
    fun destroy()
}

interface BarcodeSdkListener {
    fun onBarcodeEvent(barcode: String)
}