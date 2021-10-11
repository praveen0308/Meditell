package com.jmm.brsap.meditell.model

data class City(
    var cityId:Int?=null,
    var cityName:String?=null,
    var otherInfo:String?=null,

    @JvmField
    var isActive:Boolean?=null,
){
    override fun toString(): String {
        return cityName.toString()
    }
}
