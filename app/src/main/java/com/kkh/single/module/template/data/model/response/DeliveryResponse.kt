package com.kkh.single.module.template.data.model.response

import kotlinx.serialization.Serializable

// success만 따지면 됨.

@Serializable
data class DeliveryResponse(
    val result: List<Unit>,
    val rsMsg: RsMsg
){
    fun isSuccess() : Boolean {
        return rsMsg.statusCode == "S"
    }
}