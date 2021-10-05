package com.jmm.brsap.meditell.model

data class Pharmacy(
    var pharmacyId:Int?=null,
    val areaId:Int?=null,
    val cityId:Int?=null,
    val pharmacyName:String?=null,
    val address :String?=null,
    val pharmacistName:String?=null,
    val contactNo:String?=null,
    val addedOn:String?=null,
    val addedBy:String?=null,
)
