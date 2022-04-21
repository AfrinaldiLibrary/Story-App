package com.example.storyapp.helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val parser = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(parser)
}