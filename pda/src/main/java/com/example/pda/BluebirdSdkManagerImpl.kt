package com.example.pda

// BluebirdSdkManagerImplImpl.kt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.pda.util.Constants
import java.io.UnsupportedEncodingException

class BluebirdSdkManagerImpl(private val context: Context) : BarcodeSdkManager {

    private var barcodeListener: BarcodeSdkListener? = null
    private var isReceiverRegistered = false
    private var barcodeHandle = -1
    private var currentStatus = ""
    private var count = 0

    override fun init() {
        currentStatus = STATUS_CLOSE
        isReceiverRegistered = false
        registerReceiver()
        openBarcodeReader()
    }

    override fun addListener(listener: BarcodeSdkListener) {
        Log.d("BluebirdSdkManager", "Listener added: $listener")
        barcodeListener = listener
    }

    override fun destroy() {
        if (currentStatus != STATUS_CLOSE) {
            val intent = Intent().apply {
                action = Constants.ACTION_BARCODE_CLOSE
                putExtra(Constants.EXTRA_HANDLE, barcodeHandle)
                putExtra(Constants.EXTRA_INT_DATA3, Constants.SEQ_BARCODE_CLOSE)
            }
            context.sendBroadcast(intent)
            currentStatus = STATUS_CLOSE
        }
        removeBarcodeListener()
        unregisterReceiver()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            if (intent == null) return
            val action = intent.action ?: return
            val handle = intent.getIntExtra(Constants.EXTRA_HANDLE, 0)
            val seq = intent.getIntExtra(Constants.EXTRA_INT_DATA3, 0)

            when (action) {
                Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA -> {
                    count++
                    val data = intent.getByteArrayExtra(Constants.EXTRA_BARCODE_DECODING_DATA)
                    val symbology = intent.getIntExtra(Constants.EXTRA_INT_DATA2, -1)
                    var result =
                        "[BarcodeDecodingData handle : $handle / count : $count / seq : $seq]\n"
                    result += "[Symbology] : $symbology\n"
                    var dataResult = ""
                    if (data != null) {
                        dataResult = String(data)
                        if (dataResult.contains("占�")) {
                            try {
                                dataResult = String(data, charset("Shift-JIS"))
                            } catch (e: UnsupportedEncodingException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    result += "[Data] : $dataResult"

                    // 리스너에게 던짐.
                    barcodeListener?.onBarcodeEvent(dataResult)
                    Log.d("BarcodeReceiver", "read Success : $result")
                }

                Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS -> {
                    Log.d("BarcodeReceiver", "Success : $seq")
                    when (seq) {
                        Constants.SEQ_BARCODE_OPEN -> {
                            barcodeHandle = handle
                            currentStatus = STATUS_OPEN
                            // barcode open 이 성공한 경우 triggerOn.
                            setTriggerOn()
                        }

                        Constants.SEQ_BARCODE_CLOSE -> currentStatus = STATUS_CLOSE
                        Constants.SEQ_BARCODE_SET_TRIGGER_ON -> currentStatus = STATUS_TRIGGER_ON
                        Constants.SEQ_BARCODE_SET_TRIGGER_OFF -> currentStatus = STATUS_OPEN
                    }
                }

                Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED -> {
                    val result = intent.getIntExtra(Constants.EXTRA_INT_DATA2, 0)
                    handleError(result, seq)
                }

                Constants.ACTION_BARCODE_CALLBACK_PARAMETER -> {
                    val parameter = intent.getIntExtra(Constants.EXTRA_INT_DATA2, -1)
                    val value = intent.getStringExtra(Constants.EXTRA_STR_DATA1)
                    Log.d(
                        "BarcodeReceiver",
                        "Get parameter result\nparameter : $parameter / value : $value"
                    )
                }

                Constants.ACTION_BARCODE_CALLBACK_GET_STATUS -> {
                    val status = intent.getIntExtra(Constants.EXTRA_INT_DATA2, 0)
                    currentStatus = when (status) {
                        0 -> STATUS_CLOSE
                        1 -> STATUS_OPEN
                        2 -> STATUS_TRIGGER_ON
                        else -> STATUS_CLOSE
                    }
                    Log.d("BarcodeReceiver", "Current Status: $currentStatus / id: $status")
                }
            }
        }
    }

    private fun removeBarcodeListener() {
        barcodeListener = null
    }

    private fun openBarcodeReader() {
        if (currentStatus == STATUS_CLOSE) {
            val intent = Intent().apply {
                action = Constants.ACTION_BARCODE_OPEN
                putExtra(Constants.EXTRA_HANDLE, barcodeHandle)
                putExtra(Constants.EXTRA_INT_DATA3, Constants.SEQ_BARCODE_OPEN)
            }
            context.sendBroadcast(intent)
        }
    }

    private fun setTriggerOn() {
        val intent = Intent().apply {
            action = Constants.ACTION_BARCODE_SET_TRIGGER
            putExtra(Constants.EXTRA_HANDLE, barcodeHandle)
            putExtra(Constants.EXTRA_INT_DATA2, 1)
            putExtra(Constants.EXTRA_INT_DATA3, Constants.SEQ_BARCODE_SET_TRIGGER_ON)
        }
        context.sendBroadcast(intent)
    }

    private fun registerReceiver() {
        if (isReceiverRegistered) return
        val filter = IntentFilter().apply {
            addAction(Constants.ACTION_BARCODE_CALLBACK_DECODING_DATA)
            addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_SUCCESS)
            addAction(Constants.ACTION_BARCODE_CALLBACK_REQUEST_FAILED)
            addAction(Constants.ACTION_BARCODE_CALLBACK_PARAMETER)
            addAction(Constants.ACTION_BARCODE_CALLBACK_GET_STATUS)
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        isReceiverRegistered = true
    }

    private fun unregisterReceiver() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(receiver)
                isReceiverRegistered = false
            } catch (e: Exception) {
                Log.e("BluebirdSdkManagerImpl", "Receiver unregister failed: ${e.message}")
            }
        }
    }

    companion object {
        private const val STATUS_CLOSE = "close"
        private const val STATUS_OPEN = "open"
        private const val STATUS_TRIGGER_ON = "trigger_on"
    }

    private fun handleError(result: Int, seq: Int) {
        when (result) {
            Constants.ERROR_BARCODE_DECODING_TIMEOUT ->
                Log.d("BarcodeReceiver", "Decode Timeout / seq : $seq")

            Constants.ERROR_NOT_SUPPORTED ->
                Log.d("BarcodeReceiver", "Not Supported / seq : $seq")

            Constants.ERROR_BARCODE_ERROR_USE_TIMEOUT -> {
                currentStatus = STATUS_CLOSE
                Log.d("BarcodeReceiver", "Use Timeout / seq : $seq")
            }

            Constants.ERROR_BARCODE_ERROR_ALREADY_OPENED -> {
                currentStatus = STATUS_OPEN
                Log.d("BarcodeReceiver", "Already opened / seq : $seq")
            }

            Constants.ERROR_BATTERY_LOW -> {
                currentStatus = STATUS_CLOSE
                Log.d("BarcodeReceiver", "Battery low / seq : $seq")
            }

            Constants.ERROR_NO_RESPONSE -> {
                Log.d("BarcodeReceiver", "No response / seq : $seq")
                currentStatus = STATUS_CLOSE
            }

            else -> Log.d("BarcodeReceiver", "Error $result / seq : $seq")
        }

        if (seq == Constants.SEQ_BARCODE_SET_PARAMETER &&
            result == Constants.ERROR_BARCODE_EXCEED_ASCII_CODE
        ) {
            Log.d("BarcodeReceiver", "Exceed range of ascii code")
        }
    }


}
