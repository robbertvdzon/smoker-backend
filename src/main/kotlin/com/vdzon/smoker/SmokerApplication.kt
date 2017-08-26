package com.vdzon.smoker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.cloud.context.config.annotation.RefreshScope

@SpringBootApplication
@RefreshScope
@Import(WebFluxAutoConfiguration.WebFluxConfig::class)
class SmokerApplication

fun main(args: Array<String>) {
    SpringApplication.run(SmokerApplication::class.java, *args)
}
