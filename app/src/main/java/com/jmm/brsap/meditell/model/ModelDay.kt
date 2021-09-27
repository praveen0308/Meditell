package com.jmm.brsap.meditell.model

data class ModelDay(
    val id:Int?=null,
    val dayNumber:Int?=null,
    val dayName:String?=null,
    val date:String?=null,
    val dateValue:String?=null,
    val monthNumber:Int?=null,
    val monthName:String?=null,
    val dateYear:String?=null,
    var isActive:Boolean?=null,
    val isToday:Boolean?=null

)
