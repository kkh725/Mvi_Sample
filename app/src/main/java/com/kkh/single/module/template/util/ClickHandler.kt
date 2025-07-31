package com.kkh.single.module.template.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

class DebugClickHandler(private val context: Context) {

    private var clickCount = 0
    private val maxCount = 5

    fun onDebugClick() {
        clickCount++
        if (clickCount >= maxCount) {
            clickCount = 0 // reset
            sendLogFileViaEmail(context)
        }
    }

    fun sendLogFileViaEmail(context: Context) {
        val file = File(context.filesDir, "http_logs.txt")
//        if (!file.exists()) return

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("your_email@example.com")) // 받는 사람
            putExtra(Intent.EXTRA_SUBJECT, "HTTP Log File")
            putExtra(Intent.EXTRA_TEXT, "첨부된 로그 파일을 확인해주세요.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // test 용
        context.startActivity(intent)
//        context.startActivity(Intent.createChooser(intent, "Send Email"))
    }
}
