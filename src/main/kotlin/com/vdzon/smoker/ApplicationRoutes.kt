package com.vdzon.smoker

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


object ApplicationRoutes {

    fun routes(logDao: SmokerLogDao?, smokerProperties:SmokerProperties?): RouterFunction<*> {
        return (route(GET("/getlast"),
                HandlerFunction { r -> getLastSample(logDao) }
        ))
                .and(route(GET("/getall"),
                        HandlerFunction { r -> getAll(r, logDao) }
                ))
                .and(route(GET("/test"),
                        HandlerFunction { r ->
                            val just = Mono.just(smokerProperties!!.exampleProperty!!)
                            ServerResponse.ok().body(just, String::class.java) }
                ))
                .and(route(GET("/add"),
                        HandlerFunction { r -> add(r, logDao) }
                ))

    }

    private fun add(r: ServerRequest?, logDao: SmokerLogDao?): Mono<ServerResponse> {
        val tempString = r!!.queryParam("temp");
        val sturingString = r!!.queryParam("sturing");
        val temp = tempString.map { Integer.valueOf(it) }.orElse(0)
        val sturing = sturingString.map { Integer.valueOf(it) }.orElse(0)
        val domain = SmokerLog(UUID.randomUUID(), Date(), temp, sturing)
        val added = logDao!!.save(domain)
        val body: Mono<ServerResponse> = ServerResponse.ok().body(added, SmokerLog::class.java)
        return body
    }

    private fun getAll(r: ServerRequest?, logDao: SmokerLogDao?): Mono<ServerResponse> {
        val range = r!!.queryParam("range");
        val findAll: Flux<SmokerLogDto> =
                if (range.isPresent) {
                    logDao!!.getRange(range.get()).map { SmokerLogDto.fromSmokerLog(it) }
                } else {
                    logDao!!.findAll().map { SmokerLogDto.fromSmokerLog(it) }
                };
        val body = ServerResponse.ok().body(findAll, SmokerLogDto::class.java)
        return body
    }

    private fun getLastSample(logDao: SmokerLogDao?): Mono<ServerResponse> {
        val lastLog = logDao!!.getLastSample()
        val lastLogDto = lastLog.map { SmokerLogDto.fromSmokerLog(it)!! }
        val body: Mono<ServerResponse> = ServerResponse.ok().body(lastLogDto, SmokerLogDto::class.java)
        return body
    }

}