package com.vdzon.smoker

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SmokerProperties {

    @Value("\${example.property}")
    val exampleProperty: String? = null

    @Value("\${application.name}")
    val applicationName: String? = null

    @Value("\${build.version}")
    val buildVersion: String? = null

    @Value("\${build.timestamp}")
    val buildTimestamp: String? = null
}
