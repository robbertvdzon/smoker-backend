package com.vdzon.smoker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*


@Component
class SmokerLogDao() {

    data class SmokerLog2(
            val date: Date,
            val temp: Int,
            val sturing: Int
    )


    @Autowired
    val smokerlogRepository: SmokerlogRepository? = null;

    @Autowired
    var mongoTemplate: ReactiveMongoTemplate? = null


    fun getRange(range: String): Flux<SmokerLog> {
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
                            .and("sturing").`as`("sturing")
                    ,
                    Aggregation.group("dayOfYear", "hour", "minute")
                            .avg("temp")
                            .`as`("temp")
                            .first("date")
                            .`as`("date")
                            .avg("sturing")
                            .`as`("sturing")
                    ,
                    Aggregation.sort(Sort.Direction.DESC, "date"),
                    Aggregation.match(Criteria.where("date").gt(startTime)),
                    Aggregation.match(Criteria.where("date").lt(endTime))
            )
            //Convert the aggregation result into a List
            val aggregate = mongoTemplate!!.aggregate(agg, SmokerLog::class.java, SmokerLog2::class.java)
            return aggregate.map { SmokerLog(id = UUID.randomUUID(), date = it.date, temp = it.temp, sturing = it.sturing) }
        } else
            smokerlogRepository!!.findByDateBetweenReduced(startTime, endTime, Sort(Sort.Direction.DESC, "date"))
    }


    fun save(log: SmokerLog) {
        smokerlogRepository!!.save(log).block()
    }

    fun findAll() = smokerlogRepository!!.findAll(Sort(Sort.Direction.DESC, "date"))

    fun getLastSample(): Mono<SmokerLog> {
        return smokerlogRepository!!.findFirst(Sort(Sort.Direction.DESC, "date"));
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

        return when {
            range.equals("2uur") -> minusHours(2)
            range.equals("8uur") -> minusHours(8)
            range.equals("24uur") -> minusHours(24)
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


}