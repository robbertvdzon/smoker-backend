package com.vdzon.smoker

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SmokerProperties {

    @Value("\${configuratie.profiel}")
    val configuratieProfiel: String? = null

    @Value("\${build.version}")
    val buildVersion: String? = null
}
