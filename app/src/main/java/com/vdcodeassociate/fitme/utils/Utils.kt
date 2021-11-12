package com.vdcodeassociate.fitme.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import com.vdcodeassociate.fitme.room.Run
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

    fun getTimeInWords(ms: Long): String{
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(hours != 0L){
            return "$hours Hours $minutes Minutes $seconds Seconds"
        }else if(minutes != 0L){
            return "$minutes Minutes $seconds Seconds"
        }else if(seconds != 0L){
            return "$seconds Seconds"
        }else {
            return ""
        }
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

    // date format used in run and statistics
    fun DateFormatRuns(run: Run, inRun: Boolean): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }

        val dateFormat = if(inRun) SimpleDateFormat("EEEE, d MMM, yyyy", Locale.getDefault())
        else SimpleDateFormat("d MMM, yy", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    // date format used in run and statistics
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