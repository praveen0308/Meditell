package com.jmm.brsap.meditell.model

data class Pharmacy(
    var pharmacyId:Int,
    val areaId:Int,
    val pharmacyName:String,
    val address :String,
    val pharmacistName:String,
    val contactNo:String,
    val addedOn:String,
    val addedBy:String
)
