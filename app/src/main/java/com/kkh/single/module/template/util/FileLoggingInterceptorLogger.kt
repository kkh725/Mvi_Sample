package com.kkh.single.module.template.util

import android.content.Context
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingInterceptorLogger(private val context: Context) : HttpLoggingInterceptor.Logger {

    private val logFile: File by lazy {
        File(context.filesDir, "http_logs.txt").apply {
            deleteIfOld()
        }
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    override fun log(message: String) {
        try {
            val timestamp = timestampFormat.format(Date())
            FileWriter(logFile, true).use { writer ->
                writer.appendLine("$timestamp - $message")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun File.deleteIfOld() {
        if (exists()) {
            val lastModifiedDate = dateFormat.format(Date(lastModified()))
            val todayDate = dateFormat.format(Date())

            if (lastModifiedDate != todayDate) {
                delete()  // 어제 이전 파일이면 삭제
                createNewFile()
            }
        }
    }
}
