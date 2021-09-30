package com.jmm.brsap.meditell.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// Default
val SDF_DMY_WITH_DASH = SimpleDateFormat("dd-MM-yyyy", Locale.US)
val SDF_DMYHMS = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
val SDF_YMD_WITH_DASH = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val SDF_EDMY = SimpleDateFormat("EE, dd MMM yyyy", Locale.US)

fun getDaysBetweenDates(startDate: String, endDate: String): ArrayList<String> {
    val d1: Date = SDF_DMY_WITH_DASH.parse(startDate)!!
    val d2: Date = SDF_DMY_WITH_DASH.parse(endDate)!!

    val d3 : Date = getDateAfterCountDays(d2,1)
    val dates = ArrayList<String>()
    val calendar: Calendar = GregorianCalendar()
    calendar.time = d1
    while (calendar.time.before(d3)) {
        dates.add(SDF_DMY_WITH_DASH.format(calendar.time))
        calendar.add(Calendar.DATE, 1)
    }
    return dates
}

fun convertDMY2EMDY(date:String):String{
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val date = formatter.parse(date)
    val desiredFormat = SimpleDateFormat("EE,MMM dd yyyy").format(date)
    return desiredFormat
}
fun convertEpochTimeToDate(seconds:Long,sdf:SimpleDateFormat = SDF_EDMY):String{
    return sdf.format(Date(seconds * 1000L))
}

fun convertSecondsTimeToDate(seconds:Long,sdf:SimpleDateFormat = SDF_DMY_WITH_DASH):String{
    return sdf.format(Date(seconds))
}

fun getTodayDate():String{
    return getDaysAgo(0)
}


fun getDaysAgo(daysAgo: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, daysAgo)
    return SDF_DMY_WITH_DASH.format(calendar.time)
}

/*
fun getDatePlusDays(startDate: String, days: Int): ArrayList<String>{
    val d1: Date = SDF_DMY_WITH_DASH.parse(startDate)!!
    return getDaysBetweenDates(d1, getDateAfterCountDays(d1, days))
}*/

fun getDateAfterCountDays(date: Date, days: Int): Date {
    val c = Calendar.getInstance()
    c.time = date // Now use today date.
    c.add(Calendar.DATE, days) // Adding n days
    return c.time
}

fun getMonthName(num:Int):String{
    return when(num){
        0->"January"
        1->"February"
        2->"March"
        3->"April"
        4->"May"
        5->"June"
        6->"July"
        7->"August"
        8->"September"
        9->"October"
        10->"November"
        11->"December"
        else->""
    }
}


fun getDayName(num:Int):String{
    return when(num){
        1->"Sunday"
        2->"Monday"
        3->"Tuesday"
        4->"Wednesday"
        5->"Thursday"
        6->"Friday"
        7->"Saturday"
        else->""
    }
}