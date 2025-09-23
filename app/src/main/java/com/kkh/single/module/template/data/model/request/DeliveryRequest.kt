package com.kkh.single.module.template.data.model.request

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class DeliveryRequest(
    val parameter: List<ParameterItem>
)

@Serializable
data class ParameterItem(
    val patId: String,     // 환자번호
    val ward: String,      // 소속병동
    val dlvrDt: String = nowForSqno(),    // 배송일자
    val dlvrDvcd: String,  // 배송구분  0,1 로 구분. 0은 start, 1은 arrive 예상. 그런데 int가 아닐지?
    val dprtDept: String,  // 출발과
    val arvlDept: String   // 도착과
){
    companion object {
        fun nowForSqno(): String {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
            return now.format(formatter)
        }
    }
}