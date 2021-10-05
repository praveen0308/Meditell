package com.jmm.brsap.meditell.model

data class Doctor(
    var doctorId:Int?=null,
    val areaId:Int?=null,
    val cityId:Int?=null,
    val name:String?=null,
    val address:String?=null,
    val degree:Int?=null,
    val dateOfBirth:String?=null,
    val contactNo:String?=null,
    val addedOn:String?=null,
    val addedBy:String?=null
)
