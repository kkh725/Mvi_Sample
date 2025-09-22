package com.kkh.single.module.template.domain.repository

interface MainRepository {
    suspend fun setUserInfo(userName: String): Result<Unit>
    suspend fun sendDeliveryInfo() : Result<Unit>

    suspend fun fetchDept(dept : String)
    suspend fun getDept() : String
}