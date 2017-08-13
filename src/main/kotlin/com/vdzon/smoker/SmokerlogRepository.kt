package com.vdzon.smoker

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface SmokerlogRepository : ReactiveCrudRepository<SmokerLog, Long> {

    fun findFirstByDate(): Mono<SmokerLog>


}