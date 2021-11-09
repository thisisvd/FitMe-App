package com.vdcodeassociate.fitme.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    var vibrantLightColorList = arrayOf(
        ColorDrawable(Color.parseColor("#ffeead")),
        ColorDrawable(Color.parseColor("#93cfb3")),
        ColorDrawable(Color.parseColor("#fd7a7a")),
        ColorDrawable(Color.parseColor("#faca5f")),
        ColorDrawable(Color.parseColor("#1ba798")),
        ColorDrawable(Color.parseColor("#6aa9ae")),
        ColorDrawable(Color.parseColor("#ffbf27")),
        ColorDrawable(Color.parseColor("#d93947"))
    )

    fun getRandomDrawableColor(): ColorDrawable? {
        val idx: Int = Random().nextInt(vibrantLightColorList.size)
        return vibrantLightColorList[idx]
    }

    fun DateToTimeFormat(oldstringDate: kotlin.String?): kotlin.String? {
        //        val p = (Locale(getCountry()))
        var isTime: kotlin.String? = null
        val newDate: kotlin.String?

        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )

//            val date: Date = sdf.parse(oldstringDate)
//            isTime = p.format(date)

            val time = sdf.parse(oldstringDate).time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            isTime = ago.toString()

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return isTime
    }

    fun DateFormat(oldstringDate: kotlin.String?): kotlin.String? {
        val newDate: kotlin.String?
        val dateFormat = SimpleDateFormat("EEEE, d MMM, yyyy", Locale(getCountry()))
        newDate = try {
            val date: Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(oldstringDate)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

    fun getCountry(): String? {
        val locale: Locale = Locale.getDefault()
        val country: String = locale.country
        return country.toLowerCase()
    }

}