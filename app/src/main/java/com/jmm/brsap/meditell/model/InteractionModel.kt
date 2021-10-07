package com.jmm.brsap.meditell.model

import com.google.firebase.firestore.Exclude

data class InteractionModel(
    var interactionId:Int?=null,
    var areaId:Int?=null,
    var interactedWith:Int?=null,
    var interactionWasWith:String?=null,
    var type:String?=null,
    var interactedBy:String?=null,
    var imageUrl:String?=null,
    var summary:String?=null,
    var dateTime:String?=null,
    var interactedOn:String?=null,


    // extras
    @Exclude @set:Exclude @get:Exclude
    var interactedWithModel:Any?=null
)
