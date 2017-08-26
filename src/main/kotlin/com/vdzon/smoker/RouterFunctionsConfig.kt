package com.vdzon.smoker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction

@Configuration
class RouterFunctionsConfig {

    @Autowired
    val smokerLogDao: SmokerLogDao? = null;

    @Autowired
    val smokerProperties:SmokerProperties? = null;

    @Bean
    fun routerFunctions(): RouterFunction<*> {
        return ApplicationRoutes.routes(smokerLogDao, smokerProperties)
    }
}