package com.vdzon.smoker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.context.config.annotation.RefreshScope

@SpringBootApplication
@RefreshScope
class SmokerApplication

fun main(args: Array<String>) {
    SpringApplication.run(SmokerApplication::class.java, *args)
}
