package com.kkh.single.module.template.data.repository

import com.kkh.single.module.template.data.datasource.local.LocalDataSource
import com.kkh.single.module.template.data.datasource.remote.RemoteDataSource
import com.kkh.single.module.template.data.model.processApiResponse
import com.kkh.single.module.template.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {
    //business
    override suspend fun setUserInfo(userName: String) =
        runCatching {
            remoteDataSource.fetchUserInfo().processApiResponse()
        }

    override suspend fun sendDeliveryInfo() =
        runCatching {
            remoteDataSource.sendDeliveryInfo().processApiResponse()
        }

    /**
     * Local
     * @param dept 부서명 - 약제실, 42병동, 중환자실 등
     */

    override suspend fun fetchDept(dept: String) {
        localDataSource.fetchDept(dept)
    }

    override suspend fun getDept() : String{
        return localDataSource.getDept()
    }
}