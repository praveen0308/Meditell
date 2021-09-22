package com.jmm.brsap.meditell.model

data class SalesRepresentative(
    val firstName : String,
    val middleName : String,
    val lastName : String,
    val dateOfBirth : String,
    val email : String,
    val mobileNo : String,
    val userName : String,
    val password : String,
    val lastLoggedIn : String,
    val isActive : Boolean,
)
