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


    fun routes(smokerlogRepository: SmokerlogRepository?): RouterFunction<*> {
        return (route(GET("/getlast"),
                HandlerFunction { r -> getMono(smokerlogRepository) }
        ))
                .and(route(GET("/getall"),
                        HandlerFunction { r -> getFlux(r, smokerlogRepository) }
                ))
                .and(route(GET("/add"),
                        HandlerFunction { r -> add(r, smokerlogRepository) }
                ))

    }

    private fun add(r: ServerRequest?, smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
        val tempString = r!!.queryParam("temp");
        val sturingString = r!!.queryParam("sturing");
        val temp = tempString.map { Integer.valueOf(it) }.orElse(0)
        val sturing = sturingString.map { Integer.valueOf(it) }.orElse(0)
        val domain: SmokerLog = SmokerLog(UUID.randomUUID(), Date(), temp, sturing)
        smokerlogRepository!!.save(domain).block()
        val body: Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("ok"), String::class.java)
        return body
    }

    private fun getFlux(r: ServerRequest?, smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
        val range = r!!.queryParam("range");
        val findAll: Flux<SmokerLog> =
                if (range.isPresent) {
                    println(range.get())
                    smokerlogRepository!!.findAll()
                } else {
                    smokerlogRepository!!.findAll()
                };
        val body = ServerResponse.ok().body(findAll, SmokerLog::class.java)
        return body
    }


    private fun getMono(smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
//        val body: Mono<ServerResponse> = ServerResponse.ok().body(getFirstByDomain(smokerlogRepository), SmokerLog::class.java)
        val body: Mono<ServerResponse> = ServerResponse.ok().body(Mono.just(SmokerLog(UUID.randomUUID(),Date(),12,13)), SmokerLog::class.java)
        return body
    }

    private fun getFirstByDomain(smokerlogRepository: SmokerlogRepository?): Mono<SmokerLog> {
        val res: Mono<SmokerLog> = smokerlogRepository!!.findFirstByDate()
        return res
    }

}