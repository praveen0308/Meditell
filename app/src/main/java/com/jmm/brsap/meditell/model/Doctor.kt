package com.jmm.brsap.meditell.model

data class Doctor(
    val doctorId:Int,
    val areaId:Int,
    val name:String,
    val address:String,
    val degree:String,
    val dateOfBirth:String,
    val age:Int,
    val contactNo:String,
    val addedOn:String,
    val addedBy:String
)
