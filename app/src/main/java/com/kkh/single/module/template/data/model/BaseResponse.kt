package com.kkh.single.module.template.data.model

import kotlinx.serialization.Serializable
import retrofit2.Response

// -------------------------
// 1. 공통 Response 모델
// -------------------------
@Serializable
data class ApiResponse<T>(
    val result: List<ResultItem<T>> = emptyList(),
    val rsMsg: RsMsg
) {
    fun isSuccess(): Boolean = rsMsg.statusCode == "S"
    fun isError(): Boolean = rsMsg.statusCode == "E"
}

@Serializable
data class ResultItem<T>(
    val parameter: List<T> = emptyList()
)

@Serializable
data class RsMsg(
    val message: String,
    val statusCode: String,   // "S" 성공, "E" 에러
    val errorCode: String? = null
)

// -------------------------
// 2. HTTP / API 에러 처리
// -------------------------
enum class HttpErrorStatus(val code: Int, val message: String) {
    UNAUTHORIZED(401, "인증 오류"),
    DUPLICATE(409, "중복 에러"),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류")
}

fun handleHttpError(code: Int): Exception {
    return when (code) {
        HttpErrorStatus.UNAUTHORIZED.code -> IllegalAccessException(HttpErrorStatus.UNAUTHORIZED.message)
        HttpErrorStatus.DUPLICATE.code -> IllegalStateException(HttpErrorStatus.DUPLICATE.message)
        HttpErrorStatus.INTERNAL_SERVER_ERROR.code -> Exception(HttpErrorStatus.INTERNAL_SERVER_ERROR.message)
        else -> Exception("HTTP 오류 코드: $code")
    }
}

// -------------------------
// 3. Retrofit Response 처리 확장함수
// -------------------------
inline fun <reified T> Response<ApiResponse<T>>.processApiResponse(): List<T> {
    if (this.isSuccessful) {
        val body = this.body() ?: throw NullPointerException("응답 바디가 null입니다.")

        if (body.isSuccess()) {
            if (body.result.isEmpty()) throw NoSuchElementException("데이터가 없습니다.")
            // result 안에 parameter 리스트 합쳐서 반환
            else{
                return body.result.flatMap { it.parameter }
            }
        } else {
            throw handleHttpError(500)
        }
    } else {
        throw handleHttpError(this.code())
    }
}


typealias BaseApiResponse<T> = Response<ApiResponse<T>>

