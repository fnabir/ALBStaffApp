package com.asianliftbd.staff.services

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabasePersistence : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
    }
}