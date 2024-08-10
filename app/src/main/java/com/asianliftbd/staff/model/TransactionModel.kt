package com.asianliftbd.staff.model

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class TransactionModel (
    var name: String? = null,
    var title: String = "",
    var details: String? = null,
    var amount: Int = 0,
    var date: String = "")