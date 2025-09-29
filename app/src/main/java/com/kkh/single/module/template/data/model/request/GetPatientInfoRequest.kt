package com.kkh.single.module.template.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PatientInfoRequest(
    val parameter: List<UserParameterItem>
)

@Serializable
data class UserParameterItem(
    val patId: String,   // 환자 번호
    val userId: String = "",  // 사용자 ID // TODO 사용자 아이디를 받아올 수없음. patId가 전부.
    val eqpmId: String = "PM30",  // 장비 ID TODO PDA NO 조회해야할듯
    val coId: String = "PNT",    // 회사 코드
    val rmrk: String = ""     // 비고
)