package com.vdzon.smoker

import java.util.*

data class SmokerLogDto(
        val userid: String,
        val date: Date,
        val temp: Int,
        val sturing: Int
){
    companion object {
        fun fromSmokerLog(smokerLog:SmokerLog?):SmokerLogDto?=
            if (smokerLog==null) null else SmokerLogDto(userid = smokerLog.userid, date=smokerLog.date, temp = smokerLog.temp, sturing = smokerLog.sturing)

    }
}