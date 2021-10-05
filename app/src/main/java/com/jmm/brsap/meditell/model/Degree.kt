package com.jmm.brsap.meditell.model

data class Degree(
    var degreeId:Int?=null,
    var name:String?=null
){
    override fun toString(): String {
        return name.toString()
    }
}
