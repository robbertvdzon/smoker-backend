package com.vdzon.smoker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.User
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class SmokerResource {

    @Autowired
    val smokerLogDao: SmokerLogDao? = null;

    @Autowired
    private val connectionRepository: ConnectionRepository? = null

    @Autowired
    val smokerProperties: SmokerProperties? = null;

    @RequestMapping("/getall")
    private fun getAll(@RequestParam(value = "range", defaultValue = "") range: String): List<SmokerLogDto?> {
        val findAll =
                if (!range.isEmpty()) {
                    smokerLogDao!!.getRange(range).map { SmokerLogDto.fromSmokerLog(it) }
                } else {
                    smokerLogDao!!.findAll().map { SmokerLogDto.fromSmokerLog(it) }
                };
        return findAll
    }

    @RequestMapping("/getlast")
    private fun getLastSample(): SmokerLogDto {
        val lastLog = smokerLogDao!!.getLastSample()
        val lastLogDto = SmokerLogDto.fromSmokerLog(lastLog)!!
        return lastLogDto
    }

    @RequestMapping("/add")
    fun add(@RequestParam(value = "temp", defaultValue = "0") temp: Int, @RequestParam(value = "sturing", defaultValue = "0") sturing: Int): SmokerLog {
        val domain = SmokerLog(UUID.randomUUID(), Date(), temp, sturing)
        return smokerLogDao!!.save(domain)
    }

    @RequestMapping("/user")
    fun user(): User? {
        val primaryConnection = connectionRepository!!.findPrimaryConnection(Facebook::class.java) ?: return null
        val fields = arrayOf("id", "email", "first_name", "last_name")
        return primaryConnection.api.fetchObject("me", User::class.java, *fields)
    }

    @RequestMapping("/test")
    fun prop(): String? {
        return smokerProperties!!.exampleProperty
    }


}