package com.jmm.brsap.meditell.model

import com.jmm.brsap.meditell.util.NavigationEnum

data class WidgetModel(
    val id:NavigationEnum,
    val title:String,
    val imgUrl:Int,
)
