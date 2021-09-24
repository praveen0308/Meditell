package com.jmm.brsap.meditell.model

import com.google.firebase.Timestamp

data class Schedule (
    var date:Timestamp = Timestamp.now(),
    val areaVisits:List<String> = listOf()
) {

}
