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
@RequestMapping("/api")
class SmokerResource {

    @Autowired
    val smokerLogDao: SmokerLogDao? = null

    @Autowired
    val userDao: UserDao? = null

    @Autowired
    private val connectionRepository: ConnectionRepository? = null

    @Autowired
    val smokerProperties: SmokerProperties? = null;

    @RequestMapping("/getall")
    private fun getAll(@RequestParam(value = "userid") userid: String, @RequestParam(value = "range", defaultValue = "2uur") range: String): List<SmokerLogDto?>? {
        println("get all, userid="+userid)
        val smokerUser: SmokerUser? = userDao!!.findByUserid(userid)
        val openbaar = smokerUser?.openbaar?:false
        if (!openbaar){
            println("get all, user required")
            val user: User? = user();
            if (user==null) throw Exception("Niet ingelogd"); // TODO: geef een unauthorized terug!
            if (user.id!=userid)  throw Exception("Unauthorized"); // TODO: geef een unauthorized terug!
        }
        val findAll = smokerLogDao!!.getRange(userid, range)?.map { SmokerLogDto.fromSmokerLog(it) }
        return findAll
    }

    @RequestMapping("/getlast")
    private fun getLastSample(@RequestParam(value = "userid") userid: String): SmokerLogDto? {
        println("get last, userid="+userid)
        val smokerUser: SmokerUser? = userDao!!.findByUserid(userid)
        val openbaar = smokerUser?.openbaar?:false
        if (!openbaar){
            println("get all, user required")
            val user: User? = user();
            if (user==null) throw Exception("Niet ingelogd"); // TODO: geef een unauthorized terug!
        }
        val lastLog = smokerLogDao!!.getLastSample(userid)
        val lastLogDto = if (lastLog==null) null else SmokerLogDto.fromSmokerLog(lastLog)!!
        return lastLogDto
    }

    @RequestMapping("/add")
    fun add(@RequestParam(value = "uploadAuthKey") uploadAuthKey: String, @RequestParam(value = "userid") userid: String, @RequestParam(value = "temp", defaultValue = "0") temp: Int, @RequestParam(value = "sturing", defaultValue = "0") sturing: Int): SmokerLog? {
        val user: User? = user();
        if (user==null) throw Exception("Niet ingelogd"); // TODO: geef een unauthorized terug!
        val smokerUser: SmokerUser = findOrCreateUser(user.id)
        if (uploadAuthKey!=smokerUser.uploadAuthKey) throw Exception("Ongeldige auth key"); // TODO : geen unauthorized terug
        val smokerLog = SmokerLog(UUID.randomUUID(), userid, Date(), temp, sturing)
        return smokerLogDao!!.save(smokerLog)
    }

    fun findOrCreateUser(id:String):SmokerUser{
        val smokerUser: SmokerUser? = userDao!!.findByUserid(id)
        if (smokerUser!=null) return smokerUser;
        val newSmokerUser: SmokerUser = SmokerUser(userid = id, uploadAuthKey = UUID.randomUUID().toString(), openbaar = false, requiredTemp = 110);
        return userDao!!.save(newSmokerUser)
    }

    @RequestMapping("/user")
    fun user(): User? {
        val primaryConnection = connectionRepository!!.findPrimaryConnection(Facebook::class.java) ?: return null
        val fields = arrayOf("id", "email", "first_name", "last_name")
        return primaryConnection?.api.fetchObject("me", User::class.java, *fields)
    }

    data class Status(val userid: String?,val username: String?,val authenticated: Boolean, val version:String?, val profiel:String?, val user:SmokerUser?, val lastTemp: Int?, val requiredTemp: Int?, val lastUpdate: Date?, val openbaar: Boolean?)
    @RequestMapping("/getstatus")
    fun getstatus(): Status {
        val user: User? = user();
        val smokerUser:SmokerUser? = if (user==null) null else findOrCreateUser(user.id)
        val lastSample = if (user==null) null else getLastSample(user.id)
        return Status(username = user?.firstName,
                userid = user?.id,
                authenticated = user!=null,
                version = smokerProperties!!.buildVersion,
                profiel =  smokerProperties!!.configuratieProfiel,
                user = smokerUser,
                lastTemp = lastSample?.temp,
                lastUpdate = lastSample?.date,
                requiredTemp = smokerUser?.requiredTemp,
                openbaar = smokerUser?.openbaar
        )
    }

    @RequestMapping("/setSettings")
    fun setSettings(@RequestParam(value = "key") authKey: String, @RequestParam(value = "openbaar") openbaar: Boolean) {
        val user: User? = user();
        // TODO: geef een unauthorized terug als er geen user is
        if (user!=null) {
            val smokerUser: SmokerUser = findOrCreateUser(user.id)
            val newSmokerUser: SmokerUser = smokerUser.copy(uploadAuthKey = authKey, openbaar = openbaar)
            userDao!!.save(newSmokerUser)
        }
    }

    @RequestMapping("/clearData")
    fun clearData() {
        val user: User? = user();
        // TODO: geef een unauthorized terug als er geen user is
        if (user!=null) {
            smokerLogDao!!.clearData()
        }
    }


    data class AddTempStatus(val requiredTemp: Int?)
    @RequestMapping("/addreqtemp")
    fun addReqTemp(@RequestParam(value = "add") add: Int):AddTempStatus {
        val user: User? = user();
        // TODO: geef een unauthorized terug als er geen user is
        if (user!=null) {
            val smokerUser: SmokerUser = findOrCreateUser(user.id)
            val newSmokerUser: SmokerUser = smokerUser.copy(requiredTemp = (smokerUser.requiredTemp?:0)+add)
            userDao!!.save(newSmokerUser)
            return AddTempStatus(newSmokerUser.requiredTemp);
        }
        return AddTempStatus(0);
    }




}