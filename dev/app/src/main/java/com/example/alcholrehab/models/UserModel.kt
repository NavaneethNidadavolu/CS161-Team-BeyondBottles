package com.example.alcholrehab.models

import com.google.gson.annotations.SerializedName

class UserModel {

    @SerializedName("id")
    var userId : Int = 0

    @SerializedName("mobile")
    var email : String = ""

    @SerializedName("username")
    var username : String = ""

    @SerializedName("error")
    var password : String = ""

}
