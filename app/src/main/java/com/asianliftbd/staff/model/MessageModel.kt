package com.asianliftbd.staff.model
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageModel (
    val title: String? = null,
    val text: String? = null,
    val background: String? = null,
    val show: Boolean = false)