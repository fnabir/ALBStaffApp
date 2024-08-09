package com.asianliftbd.staff

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.SystemClock
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs


object Utils {

    private var c: Long = 0
    fun amountFormat(number: Int): String {
        val amount = NumberFormat.getInstance()
        if (amount is DecimalFormat) {
            amount.applyPattern("#,##,##,###")
            amount.isGroupingUsed = true
            val absNumber = abs(number)
            return if (number < 0) {
                String.format("- ৳ %s", amount.format(absNumber.toLong()))
            } else String.format("৳ %s", amount.format(absNumber.toLong()))
        }
        return "৳ 0"
    }

    fun amountFormatTextView(t: TextView?, number: Int, prefix: String? = "",) {
        t?.text = String.format("%s %s", prefix, amountFormat(number))
    }

    fun singleClick() {
        if (SystemClock.elapsedRealtime() - c < 1000) {
            return
        }
        c = SystemClock.elapsedRealtime()
    }

    fun setDateTextFormat(): String? {
        val c = Calendar.getInstance()
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(c.time)
    }

    fun setDateShortFormat(): String? {
        val c = Calendar.getInstance()
        return SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(c.time)
    }

    fun setDateDatabaseFormat(): String? {
        val c = Calendar.getInstance()
        return SimpleDateFormat("yyMMdd", Locale.getDefault()).format(c.time)
    }

    fun getDateDatabaseFormat(date: String): String {
        val array = date.toCharArray()
        if (array.size == 8) return "${array[6]}${array[7]}${array[3]}${array[4]}${array[0]}${array[1]}"
        else if (array.size == 10) return "${array[8]}${array[9]}${array[3]}${array[4]}${array[0]}${array[1]}"
        else return ""
    }

    fun isValidEmail(mail: CharSequence): Boolean {
        return !TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val result: Boolean
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}