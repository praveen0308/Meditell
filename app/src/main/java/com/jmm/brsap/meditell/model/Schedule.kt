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
    var isActive:Boolean =false,

    @Exclude @set:Exclude @get:Exclude
    var scheduleAreas:MutableList<Area> = mutableListOf(),

    var checkIn:String?=null,
    var checkOut:String?=null,

    /*
    * if dayStatus
    * is 0 -> representative not yet checked in
    * is 1 -> representative already checked in
    * is 2 -> representative checked out
    *
    * */

    var dayStatus:Int=0
){

}
