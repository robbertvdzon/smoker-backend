package com.vdzon.smoker.storage

import com.vdzon.smoker.model.SmokerUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserDao(
) {

    @Autowired
    val userRepository: UserRepository? = null;


    fun save(user: SmokerUser): SmokerUser {
        return userRepository!!.save(user)
    }

    fun findByUserid(userId: String) = userRepository!!.findByUserid(userId)

}