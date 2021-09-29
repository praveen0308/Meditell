package com.jmm.brsap.meditell.model

import com.google.firebase.Timestamp

data class SalesRepresentative(
    var userId:String?=null,
    val firstName : String="",
    val middleName : String="",
    val lastName : String="",
    val dateOfBirth : Timestamp= Timestamp.now(),
    val email : String="",
    val mobileNo : String="",
    val userName : String="",
    val password : String="",
    val lastLoggedIn : String="",
    val isActive : Boolean=true
)
