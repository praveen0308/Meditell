package com.jmm.brsap.meditell.model

data class InteractionModel(
    var interactionId:Int?=null,
    var areaId:Int?=null,
    var interactedWith:Int?=null,
    var type:String?=null,
    var interactedBy:String?=null,
    var imageUrl:String?=null,
    var summary:String?=null,
    var dateTime:String?=null
)
