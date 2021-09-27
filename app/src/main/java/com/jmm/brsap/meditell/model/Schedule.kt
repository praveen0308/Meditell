package com.jmm.brsap.meditell.model

import com.google.firebase.Timestamp

data class Schedule (
    val id : Timestamp ?=null,
    var date:String ?=null,
    val areaVisits:HashMap<String,String> ?=null,
    var isActive:Boolean =false
) {

}
