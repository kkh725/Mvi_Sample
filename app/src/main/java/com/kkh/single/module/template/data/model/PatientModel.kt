package com.kkh.single.module.template.data.model

data class PatientModel(val patientId : String, val dept : String){
    companion object{
        val mockList = listOf(PatientModel("1234","병동1"),
            PatientModel("2234","병동2"),
            PatientModel("3234","병동3"))
    }
}