package com.examples.food_ordering_app.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel(

    val name:String ?= null,
    val email:String ?= null,
    val password:String ?= null,
    val phone:String?= null,
    val address:String?=null
)
