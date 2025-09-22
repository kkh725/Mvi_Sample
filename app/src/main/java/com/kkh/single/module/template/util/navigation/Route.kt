package com.kkh.single.module.template.util.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object RaasGraphBaseRoute : Route

sealed interface RaasRoute : Route {
    @Serializable
    data object ScanRoute : RaasRoute
    @Serializable
    data class DeliveryRoute(val patientId : String? = null) : RaasRoute
}