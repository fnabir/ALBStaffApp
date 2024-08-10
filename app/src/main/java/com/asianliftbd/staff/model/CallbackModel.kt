package com.asianliftbd.staff.model

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class CallbackModel (
    var name: String = "",
    var uid: String? = null,
    var details: String = "",
    var date: String = "")