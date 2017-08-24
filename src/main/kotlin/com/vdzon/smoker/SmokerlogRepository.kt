package com.vdzon.smoker

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

interface SmokerlogRepository : ReactiveCrudRepository<SmokerLog, Long> {

    @Query("{}")
    fun findFirst(sort: Sort): Mono<SmokerLog>

    @Query("{'date': {\$gte: ?0, \$lte:?1 }}")
    fun findByDateBetween(startTime: Date, endTime: Date, sort: Sort): Flux<SmokerLog>

    @Query("{'date': {\$gte: ?0, \$lte:?1 }}")

    fun findByDateBetweenReduced(startTime: Date, endTime: Date, sort: Sort): Flux<SmokerLog>

    fun findAll(sort: Sort): Flux<SmokerLog>

}