package com.jmm.brsap.meditell.util

import java.text.SimpleDateFormat
import java.util.*

private val SDF_YMD_WITH_DASH = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private val SDF_EDMY = SimpleDateFormat("EE, dd MMM yyyy", Locale.US)

fun convertYMD2EMDY(date:String):String{
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = formatter.parse(date)
    val desiredFormat = SimpleDateFormat("EE,MMM dd yyyy").format(date)
    return desiredFormat
}
fun convertEpochTimeToDate(seconds:Long,sdf:SimpleDateFormat = SDF_EDMY):String{
    return sdf.format(Date(seconds * 1000L))
}

fun getTodayDate():String{
    return getDaysAgo(0)
}


fun getDaysAgo(daysAgo: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
    return SDF_YMD_WITH_DASH.format(calendar.time)
}

