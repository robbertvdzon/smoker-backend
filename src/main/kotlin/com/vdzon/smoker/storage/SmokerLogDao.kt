package com.vdzon.smoker.storage

import com.vdzon.smoker.model.SmokerLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors


@Component
class SmokerLogDao(


) {

    data class SmokerLog2(
            val userid: String,
            val date: Date,
            val temp: Int,
            val sturing: Int
    )


    @Autowired
    val smokerlogRepository: SmokerlogRepository? = null;

    @Autowired
    var mongoTemplate: MongoTemplate? = null


    fun getRange(userId: String, range: String): List<SmokerLog>? {
        if (userId.isEmpty()) return null;
        val (startTime: Date, endTime: Date) = transformDate(range)
        val reduceOutput = hoursBetween(startTime, endTime) > 2
        return if (reduceOutput) {
            val agg = Aggregation.newAggregation(
                    Aggregation.project()
                            .and("date").extractDayOfYear().`as`("dayOfYear")
                            .and("date").extractHour().`as`("hour")
                            .and("date").extractMinute().`as`("minute")
                            .and("date").`as`("date")
                            .and("temp").`as`("temp")
                            .and("userid").`as`("userid")
                            .and("sturing").`as`("sturing")
                    ,
                    Aggregation.group("dayOfYear", "hour", "minute")
                            .avg("temp")
                            .`as`("temp")
                            .first("date")
                            .`as`("date")
                            .avg("sturing")
                            .`as`("sturing")
                            .first("userid")
                            .`as`("userid")
                    ,
                    Aggregation.sort(Sort.Direction.DESC, "date"),
                    Aggregation.match(Criteria.where("date").gt(startTime)),
                    Aggregation.match(Criteria.where("date").lt(endTime)),
                    Aggregation.match(Criteria.where("userid").`is`(userId))
            )
            //Convert the aggregation result into a List
            val aggregate = mongoTemplate!!.aggregate(agg, SmokerLog::class.java, SmokerLog2::class.java)
            return aggregate.map { SmokerLog(id = UUID.randomUUID(), userid = it.userid, date = it.date, temp = it.temp, sturing = it.sturing) }
        } else {
            smokerlogRepository!!.findByDateBetween(userId, startTime, endTime, Sort(Sort.Direction.DESC, "date"))
        }
    }


    fun save(log: SmokerLog): SmokerLog {
        return smokerlogRepository!!.save(log)
    }

//    fun findAll() = smokerlogRepository!!.findAll(Sort(Sort.Direction.DESC, "date"))

    fun getLastSample(userID: String): SmokerLog? {
        /*
        DIT IS NU HEEL LELIJK GEDAAN!
         */
        val (startTime: Date, endTime: Date) = transformDate("2uur")
        if (userID.isEmpty()) return null;
        val last2Hours = smokerlogRepository!!.findByDateBetween(userID, startTime, endTime, Sort(Sort.Direction.DESC, "date"))
        return if (last2Hours.size == 0) null else last2Hours.get(0);
//        return smokerlogRepository!!.findAll().get(0)
//        return smokerlogRepository!!.findFirst(Sort(Sort.Direction.DESC, "date"));
    }

    fun getLastSamples(userID: String?, nrSamples: Long): List<SmokerLog>? {
        /*
        DIT IS NU HEEL LELIJK GEDAAN!
         */
        if (userID == null) return null;
        val (startTime: Date, endTime: Date) = transformDate("2uur")
        if (userID.isEmpty()) return null;
        val last2Hours = smokerlogRepository!!.findByDateBetween(userID, startTime, endTime, Sort(Sort.Direction.DESC, "date"))
        return last2Hours.stream().limit(nrSamples).collect(Collectors.toList())
    }

    private fun hoursBetween(startTime: Date, endTime: Date) =
            ChronoUnit.HOURS.between(toLocalDateTime(startTime), toLocalDateTime(endTime))

    fun toLocalDateTime(date: Date): LocalDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


    data class Dates(val date1: Date, val date2: Date) {}

    fun transformDate(range: String): Dates {

        fun minusHours(hours: Long): Dates {
            val endTime: Date = Date();
            val localEndDate: LocalDateTime = toLocalDateTime(endTime)
            val localStartDate = localEndDate.minusHours(hours)
            val startTime = Date.from(localStartDate.toInstant(ZoneOffset.ofHours(2)))
            return Dates(startTime, endTime)
        }

        fun fromEpoch(): Dates {
            val endTime: Date = Date();
            val startTime = Date.from(Instant.ofEpochMilli(0));
            return Dates(startTime, endTime)
        }

        return when {
            range.equals("2uur") -> minusHours(2)
            range.equals("8uur") -> minusHours(8)
            range.equals("24uur") -> minusHours(24)
            range.equals("alles") -> fromEpoch()
            range.startsWith("custom") -> {
                val pattern = "MM/dd/yyyy HH:mm"
                val simpleDateFormat = SimpleDateFormat(pattern)
                val splittedRange = range.split("_")
                if (splittedRange.size == 4) {
                    val dateStr = splittedRange.get(1);
                    val startTimeStr = splittedRange.get(2);
                    val endTimeStr = splittedRange.get(3);
                    val startDateTimeStr = "$dateStr $startTimeStr"
                    val endDateTimeStr = "$dateStr $endTimeStr"
                    val startDate = simpleDateFormat.parse(startDateTimeStr)
                    val endDate = simpleDateFormat.parse(endDateTimeStr)
                    return Dates(startDate, endDate)
                } else minusHours(2)
            }
            else -> minusHours(2)
        }
    }

    fun clearData() {
        smokerlogRepository!!.deleteAll()
    }

}