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
        return (route(GET("/getfirst"),
                HandlerFunction { r -> getMono(smokerlogRepository) }
        ))
                .and(route(GET("/getall"),
                        HandlerFunction { r -> getFlux(smokerlogRepository) }
                ))
                .and(route(GET("/add"),
                        HandlerFunction { r -> add(r, smokerlogRepository) }
                ))

    }

    private fun add(r: ServerRequest?, smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
        println("Adding!")
        val tempString = r!!.queryParam("temp");
        println("Adding 1")
        val sturingString = r!!.queryParam("sturing");
        println("Adding 2")
        val temp = tempString.map { Integer.valueOf(it) }.orElse(0)
        println("Adding 3")
        val sturing = sturingString.map { Integer.valueOf(it) }.orElse(0)
        println("Adding 4")
        val domain: SmokerLog = SmokerLog(UUID.randomUUID(), Date(), temp, sturing)
        println("Adding 5")
        smokerlogRepository!!.save(domain).block()
        println("Adding 6")
        val body: Mono<ServerResponse> = ServerResponse.ok().body(Mono.just("ok"), String::class.java)
        return body
    }

    private fun getFlux(smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
        val findAll: Flux<SmokerLog> = smokerlogRepository!!.findAll()
        val body = ServerResponse.ok().body(findAll, SmokerLog::class.java)
        return body
    }


    private fun getMono(smokerlogRepository: SmokerlogRepository?): Mono<ServerResponse> {
        val body: Mono<ServerResponse> = ServerResponse.ok().body(getFirstByDomain(smokerlogRepository), SmokerLog::class.java)
        return body
    }

    private fun getFirstByDomain(smokerlogRepository: SmokerlogRepository?): Mono<SmokerLog> {
        val res: Mono<SmokerLog> = smokerlogRepository!!.findFirstByDate()
        return res
    }

}