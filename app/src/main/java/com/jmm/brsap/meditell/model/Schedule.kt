package com.jmm.brsap.meditell.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.io.Serializable


data class Schedule (
    val id : Timestamp ?=null,
    var date:String ?=null,
    var dayCityVisit:Int ?=null,
    var areaVisits:MutableList<Int> ?=null,

    @JvmField
    @Exclude
    var isActive:Boolean =false
){

}
