package com.asianliftbd.staff.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class OfferModel (
    val name: String = "",
    var address: String? = null,
    var ptype: String? = null,
    var wtype: String = "",
    var unit: String? = null,
    var floor: String? = null,
    var shaft: String? = null,
    var person: String? = null,
    var note: String? = null,
    var refer: String? = null,
    var date: String? = null,
    var uid: String = "") {

}