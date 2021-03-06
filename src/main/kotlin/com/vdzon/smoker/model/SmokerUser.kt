package com.vdzon.smoker.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
data class SmokerUser(
        @Id
        val id: UUID = UUID.randomUUID(),
        val userid: String,
        val uploadAuthKey: String,
        val openbaar: Boolean?,
        val requiredTemp: Int?
)