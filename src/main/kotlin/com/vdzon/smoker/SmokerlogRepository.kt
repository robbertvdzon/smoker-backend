package com.vdzon.smoker

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface SmokerlogRepository : MongoRepository<SmokerLog, Long> {
    @Query("{'userid':'?0','date': {\$gte: ?1, \$lte:?2 }}")
    fun findByDateBetween(userId:String, startTime: Date, endTime: Date, sort: Sort): List<SmokerLog>

}