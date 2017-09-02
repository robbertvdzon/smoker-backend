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
    private fun getAll(@RequestParam(value = "userid") userid: String, @RequestParam(value = "range", defaultValue = "2uur") range: String): List<SmokerLogDto?>? {
        println("get all, userid="+userid)
        val findAll = smokerLogDao!!.getRange(userid, range)?.map { SmokerLogDto.fromSmokerLog(it) }
        return findAll
    }

    @RequestMapping("/getlast")
    private fun getLastSample(@RequestParam(value = "userid") userid: String): SmokerLogDto {
        println("get last, userid="+userid)
        val lastLog = smokerLogDao!!.getLastSample(userid)
        val lastLogDto = SmokerLogDto.fromSmokerLog(lastLog)!!
        return lastLogDto
    }

    @RequestMapping("/add")
    fun add(@RequestParam(value = "userid") userid: String, @RequestParam(value = "temp", defaultValue = "0") temp: Int, @RequestParam(value = "sturing", defaultValue = "0") sturing: Int): SmokerLog {
        val smokerLog = SmokerLog(UUID.randomUUID(), userid, Date(), temp, sturing)
        return smokerLogDao!!.save(smokerLog)
    }

    @RequestMapping("/user")
    fun user(): User? {
        val primaryConnection = connectionRepository!!.findPrimaryConnection(Facebook::class.java) ?: return null
        val fields = arrayOf("id", "email", "first_name", "last_name")
        return primaryConnection?.api.fetchObject("me", User::class.java, *fields)
    }

    data class Status(val userid: String?,val username: String?,val authenticated: Boolean, val version:String?, val timestamp:String?)
    @RequestMapping("/getstatus")
    fun getstatus(): Status {
        val user: User? = user();
        return Status(username = user?.firstName,
                userid = user?.id,
                authenticated = user!=null,
                version = smokerProperties!!.buildVersion,
                timestamp = smokerProperties!!.buildTimestamp
        )
    }

}