package com.example.pda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import device.common.DecodeResult
import device.common.ScanConst
import device.sdk.ScanManager

class PMBarcodeManagerImpl(private val context: Context) : BarcodeSdkManager {

    private var mScanner: ScanManager? = null
    private var decodeResult: DecodeResult? = null
    private var mScanResultReceiver: ScannerBroadcastReceiver? = null
    private val listeners = mutableSetOf<BarcodeSdkListener>()

    private var isInitialized = false

    override fun init() {
        try {
            mScanner = ScanManager()
            decodeResult = DecodeResult()
            mScanResultReceiver = ScannerBroadcastReceiver()

            mScanner?.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG)

            val filter = IntentFilter().apply {
                addAction(ScanConst.INTENT_USERMSG)
                addAction(ScanConst.INTENT_EVENT)
            }

            ContextCompat.registerReceiver(
                context,
                mScanResultReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            isInitialized = true
        } catch (e: Exception) {
            e.printStackTrace()
            // 필요하면 에러 리스너 콜백 추가
        }
    }

    override fun addListener(listener: BarcodeSdkListener) {
        listeners.add(listener)
    }

    override fun destroy() {
        try {
            if (isInitialized) {
                context.unregisterReceiver(mScanResultReceiver)
                isInitialized = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ScannerBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ScanConst.INTENT_USERMSG) {
                try {
                    val result = decodeResult?.let {
                        mScanner?.aDecodeGetResult(it.recycle())
                        it.toString()
                    } ?: "READ_FAIL"

                    if (result != "READ_FAIL") {
                        listeners.forEach { it.onBarcodeEvent(result) }
                    } else {
                        listeners.forEach { it.onBarcodeEvent("fail") }
                    }

                    Log.d("BARCODE SDK", "result=$result")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
