package com.vdzon.smoker

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "smokerdata")
data class SmokerLog(
        @Id
        val id: UUID = UUID.randomUUID(),
        val userid: String,
        val date: Date,
        val temp: Int,
        val sturing: Int

)