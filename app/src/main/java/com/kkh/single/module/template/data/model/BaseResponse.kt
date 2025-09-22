package com.kkh.single.module.template.data.model

import retrofit2.Response

data class ApiResponse<T>(
    val returnCode: String,
    val returnMessage: String,
    val datetime: String,
    val data: T?
){
    fun isSuccess(): Boolean {
        return returnCode == "200"
    }
    fun duplicatedErr() : Boolean{
        return returnCode == "409"
    }
}

enum class HttpErrorStatus(val code: Int, val message: String){
    UNAUTHORIZED(401, "인증 오류"),
    DUPLICATE(409, "중복 에러"),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류")
}

enum class ApiErrorStatus(val code: String, val message: String){

}

fun handleHttpError(code: Int): Exception {
    return when (code) {
        HttpErrorStatus.UNAUTHORIZED.code -> IllegalAccessException(HttpErrorStatus.UNAUTHORIZED.message)
        HttpErrorStatus.DUPLICATE.code -> IllegalStateException(HttpErrorStatus.UNAUTHORIZED.message)
        HttpErrorStatus.INTERNAL_SERVER_ERROR.code -> Exception(HttpErrorStatus.INTERNAL_SERVER_ERROR.message)
        else -> Exception("HTTP 오류 코드: $code")
    }
}

fun handleApiError(code: String, message: String): Exception {
    return when (code) {
        "409" -> IllegalStateException("중복된 요청입니다.")
        else -> Exception("API 에러: [$code] $message")
    }
}

inline fun <reified T> BaseApiResponse<T>.processApiResponse(): T {
    if (this.isSuccessful) {
        val body = this.body() ?: throw NullPointerException("응답 바디가 null입니다.")

        if (body.isSuccess()) {
            return body.data ?: throw NullPointerException("응답 데이터가 null입니다.")
        } else {
            throw handleApiError(body.returnCode, body.returnMessage)
        }
    } else {
        throw handleHttpError(this.code())
    }
}

typealias BaseApiResponse<T> = Response<ApiResponse<T>>

