package com.jmm.brsap.meditell.model

import com.google.firebase.firestore.Exclude

data class Area(
    var cityId:Int?=null,
    var areaId:Int?=null,
    val name:String?=null,
    val addressInfo:String?=null,
    val active:Boolean?=null,
    val addedBy:String?=null,
    val addedOn:String?=null,
    
    // custom
    @JvmField
    @Exclude
    var isSelected:Boolean = false,

    @JvmField
    @Exclude
    var isLast:Boolean = false,

    @JvmField
    @Exclude
    var dateVisit:String?=null  // this field is made for daily call recordings to track on which date we are looking for


){
    override fun toString(): String {
        return name.toString()
    }
}
