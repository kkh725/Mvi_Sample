package com.example.pda

import android.content.Context
import com.m3.sdk.scannerlib.BarcodeListener
import com.m3.sdk.scannerlib.BarcodeManager

class M3BarcodeManagerImpl(private val context: Context) : BarcodeSdkManager {

    private lateinit var mBarcodeManager: BarcodeManager
    private val listeners = mutableSetOf<BarcodeSdkListener>()

    private val mSdkListener = object : BarcodeListener {
        override fun onBarcode(barcode: String) {
            listeners.forEach { it.onBarcodeEvent(barcode) }
        }

        override fun onBarcode(barcode: String, extra: String) {
            // 필요하면 extra 정보도 넘김
            listeners.forEach { it.onBarcodeEvent(barcode) }
        }

        override fun onGetSymbology(symbology: Int, value: Int) {
            // 필요시 처리 가능
        }
    }

    override fun init() {
        mBarcodeManager = BarcodeManager(context)
        mBarcodeManager.addListener(mSdkListener)
    }

    override fun addListener(listener: BarcodeSdkListener) {
        listeners.add(listener)
    }

    override fun destroy() {
        mBarcodeManager.removeListener(mSdkListener)
        listeners.clear()
    }
}