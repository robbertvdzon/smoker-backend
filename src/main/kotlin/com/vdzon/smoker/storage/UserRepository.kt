package com.vdzon.smoker.storage

import com.vdzon.smoker.model.SmokerUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface UserRepository : MongoRepository<SmokerUser, UUID> {
    @Query("{'userid':'?0'}")
    fun findByUserid(userId: String): SmokerUser
}