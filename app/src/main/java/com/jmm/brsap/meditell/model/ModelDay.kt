package com.jmm.brsap.meditell.model

data class ModelDay(
    val id:Int,
    val dayNumber:Int,
    val dayName:String,
    val date:String,
    val dateValue:String,
    val monthNumber:Int,
    val monthName:String,
    val dateYear:String,
    var isActive:Boolean,
    val isToday:Boolean,

)
