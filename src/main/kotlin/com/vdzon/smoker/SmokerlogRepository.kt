package com.vdzon.smoker

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface SmokerlogRepository : MongoRepository<SmokerLog, Long> {
    @Query("{'date': {\$gte: ?0, \$lte:?1 }}")
    fun findByDateBetween(startTime: Date, endTime: Date, sort: Sort): List<SmokerLog>

}