package com.adyen.android.assignment.core

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.displayNotificationToUser(context: Context, notification: String?=""){
    Toast.makeText(context, "$notification", Toast.LENGTH_LONG).show()
}