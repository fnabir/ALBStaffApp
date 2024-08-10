package com.asianliftbd.admin.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseMessageReceiver : FirebaseMessagingService() {
    val uid = FirebaseAuth.getInstance().uid

}