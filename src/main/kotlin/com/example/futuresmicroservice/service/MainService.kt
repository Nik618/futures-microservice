package com.example.futuresmicroservice.service

import com.example.futuresmicroservice.dto.*
import com.example.futuresmicroservice.jpa.entities.ApplicationEntity
import com.example.futuresmicroservice.jpa.entities.ObligationEntity
import com.example.futuresmicroservice.jpa.entities.ResultEntity
import com.example.futuresmicroservice.jpa.entities.UserEntity
import com.example.futuresmicroservice.jpa.repository.ApplicationRepository
import com.example.futuresmicroservice.jpa.repository.ObligationRepository
import com.example.futuresmicroservice.jpa.repository.ResultRepository
import com.example.futuresmicroservice.jpa.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@RestController
class MainService {

    @Autowired
    lateinit var applicationRepository: ApplicationRepository

    @Autowired
    lateinit var obligationRepository : ObligationRepository

    @Autowired
    lateinit var resultRepository: ResultRepository

    @Autowired
    lateinit var userRepository: UserRepository

    var gson = Gson()

    @PostMapping("/api/createApplication")
    fun createApplication(@RequestBody request : String): Boolean? {
        var isCreate = true
        val application: Application = gson.fromJson(request, object : TypeToken<Application>() {}.type)
        if (application.idObligation != null) {
            val applicationsByObligation = applicationRepository.findByIdObligation(obligationRepository.findById(
                application.idObligation!!
            ).get())
            var summ : Long = application.count!!
            applicationsByObligation.forEach() {
                summ += it?.count!!
            }
            if (summ > obligationRepository.findById(application.idObligation!!).get().count!!)
                isCreate = false
        }

        if (isCreate) {
            val applicationEntity = ApplicationEntity().apply {
                date = LocalDateTime.now()
                count = application.count
                price = application.price
                idUser = userRepository.findById(application.idUser!!).get()
                type = application.type
                if (application.idObligation != null)
                    idObligation = obligationRepository.findById(application.idObligation!!).get()
            }
            applicationRepository.save(applicationEntity)
            return true
        }
        return false
    }

    @PostMapping("/api/updateApplication")
    fun updateApplication(@RequestBody request : String): Boolean? {
        var isCreate = true
        val application: Application = gson.fromJson(request, object : TypeToken<Application>() {}.type)
        var applicationEntity = applicationRepository.findById(application.id!!).get()

        if (application.idObligation != null) { // заявка вторична
            val applicationsByObligation = applicationRepository.findByIdObligation(obligationRepository.findById(
                application.idObligation!!
            ).get())
            var summ : Long? = applicationEntity.count?.minus(application.count!!)

            if (summ != null) {
                applicationsByObligation.forEach() {
                    summ += it?.count!!
                }
            }
            if (summ != null) {
                if (summ > obligationRepository.findById(application.idObligation!!).get().count!!)
                    isCreate = false
            }
        }

        if (isCreate) {
            applicationEntity = ApplicationEntity().apply {
                id = application.id
                date = LocalDateTime.now()
                count = application.count
                price = application.price
                idUser = userRepository.findById(application.idUser!!).get()
                type = application.type
                if (application.idObligation != null)
                    idObligation = obligationRepository.findById(application.idObligation!!).get()
            }
            applicationRepository.save(applicationEntity)
            return true
        }
        return false
    }

    @GetMapping("/api/removeApplication")
    fun removeApplication(id : Long): Boolean? {
        val applicationEntity = applicationRepository.findById(id).get()
        applicationRepository.delete(applicationEntity)
        return true
    }


    @GetMapping("/api/getApplicationsForType")
    fun getApplicationsForType(requestType : String): String? {

        val applications = Applications()
        applications.list = mutableListOf()
        val applicationEntities = applicationRepository.findAllByType(requestType)
        applicationEntities.forEach() {
            applications.list.add(Application().apply {
                id = it?.id
                date = LocalDateTime.now()
                if (it?.idUser != null)
                    idUser = it.idUser!!.id
                count = it?.count
                price = it?.price
                if (it?.idObligation != null)
                    idObligation = it.idObligation!!.id
                type = it?.type
            })
        }
        return gson.toJson(applications)
    }

    @GetMapping("/api/getApplicationById")
    fun getApplicationById(idApplication : Long): String? {

        val applicationEntity = applicationRepository.findById(idApplication).get()
            val application = Application().apply {
                id = applicationEntity.id!!
                date = LocalDateTime.now()
                if (applicationEntity.idUser != null)
                    idUser = applicationEntity.idUser!!.id
                count = applicationEntity.count
                price = applicationEntity.price
                if (applicationEntity.idObligation != null)
                    idObligation = applicationEntity.idObligation!!.id
                type = applicationEntity.type
            }
        return gson.toJson(application)
    }


    @GetMapping("/api/getObligationsForUser")
    fun getObligationsForUser(requestIdUser : Long): String? {

        val obligations = Obligations()
        obligations.list = mutableListOf()
        var obligationEntities = obligationRepository.findAll()
        val userEntity = userRepository.findById(requestIdUser).get()
        obligationEntities = if (userEntity.role.equals("buyer")) {
            obligationRepository.findAllByIdBuyer(userRepository.findById(requestIdUser).get())
        } else {
            obligationRepository.findAllByIdSeller(userRepository.findById(requestIdUser).get())
        }

        obligationEntities.forEach() {
            obligations.list.add(Obligation().apply {
                id = it.id
                date = LocalDateTime.now()
                idBuyer = it.idBuyer!!.id
                idSeller = it.idSeller!!.id
                count = it.count
                price = it.price
            })
        }
        return gson.toJson(obligations)
    }

    @GetMapping("/api/getResultsForUser")
    fun getResultsForUser(requestIdUser : Long): String? {

        val results = Results()
        results.list = mutableListOf()
        val resultEntities = resultRepository.findAllByIdUser(userRepository.findById(requestIdUser).get())

        resultEntities.forEach() {
            results.list.add(Result().apply {
                date = LocalDateTime.now()
                idUser = it?.idUser!!.id
                value = it.value
            })
        }
        return gson.toJson(results)
    }

    @PostMapping("/api/createObligation")
    fun createObligation(@RequestBody request : String): Boolean? {

        val obligationRequest: ObligationRequest = gson.fromJson(request, object : TypeToken<ObligationRequest>() {}.type)

        val userEntity = userRepository.findById(obligationRequest.idUser!!).get() // выяснили, что за юзер инициирует сделку
        val applicationEntity = applicationRepository.findById(obligationRequest.idApplication!!).get() // выяснили, что за заявка


        val thisLastPrice = applicationEntity.idObligation!!.price

        var onDelete = true
        if (obligationRequest.count!! < applicationEntity.count!!)
            onDelete = false

        // если обязательство уже существует на момент продажи
        if (applicationEntity.idObligation != null) {
            applicationEntity.idObligation!!.count = applicationEntity.idObligation!!.count?.minus(applicationEntity.count!!)
            if (!onDelete)
                obligationRepository.save(applicationEntity.idObligation!!)
            else {
                applicationRepository.delete(applicationEntity)
                obligationRepository.delete(applicationEntity.idObligation!!)
            }
        }

        // а это новое обязательство, его всё равно нужно создать
        val obligationEntity = ObligationEntity().apply {
            date = applicationEntity.date
            count = obligationRequest.count
            price = applicationEntity.price
            if (applicationEntity.idObligation != null) { // если мы дробим существующее обязательство
                price = applicationEntity.idObligation!!.price // цена обязательства не меняется
                resultRepository.save(ResultEntity().apply {
                    value = applicationEntity.price?.minus(applicationEntity.idObligation!!.price!!)?.times(applicationEntity.count!!)
                    date = LocalDateTime.now()
                    idUser = applicationEntity.idUser
                    lastPrice = thisLastPrice
                    currentPrice = applicationEntity.price
                    count = applicationEntity.count
                }) // разница в цене отправится перепродающему
            }
            if (userEntity.role.equals("buyer")) {
                idBuyer = userEntity
                idSeller = applicationEntity.idUser
            } else {
                idSeller = userEntity
                idBuyer = applicationEntity.idUser
            } // расставляем id в зависимости от типа заявки
        }
        obligationRepository.save(obligationEntity)
        if (onDelete)
            applicationRepository.delete(applicationEntity)
        else {
            applicationEntity.count = applicationEntity.count!! - obligationRequest.count
            applicationRepository.save(applicationEntity)
        }
        return true
    }

    @PostMapping("/api/createUser")
    fun createUser(@RequestBody request : String): Boolean? {

        val thisSalt = generateSalt(8)
        val user: User = gson.fromJson(request, object : TypeToken<User>() {}.type)
        val userEntity = UserEntity().apply {
            login = user.login
            password = hashPassword(user.password!!, thisSalt)?.get()
            role = user.role
            name = user.name
            salt = thisSalt
        }
        userRepository.save(userEntity)
        return true
    }

    @PostMapping("/api/checkUser")
    fun checkUser(@RequestBody request : String): Any? {

        val user: User = gson.fromJson(request, object : TypeToken<User>() {}.type)
        val userEntity = userRepository.findByLogin(user.login!!).get()
        val resultUser = User().apply {
            id = userEntity.id
            login = userEntity.login
            role = userEntity.role
            name = userEntity.name
        }

        return if (verifyPassword(user.password, userEntity.password, userEntity.salt))
            gson.toJson(resultUser)
        else false
    }

    @GetMapping("/api/getUsers")
    fun getUsers(): String? {

        val users = Users()
        users.list = mutableListOf()
        val userEntities = userRepository.findAll()

        userEntities.forEach() {
            users.list.add(User().apply {
                id = it.id
                password = it.password
                role = it.role
                login = it.login
                name = it.name
            })
        }
        return gson.toJson(users)
    }



    private val RAND: SecureRandom = SecureRandom()

    fun generateSalt(length: Int): String {
        if (length < 1) {
            System.err.println("error in generateSalt: length must be > 0")
            return ""
        }
        val salt = ByteArray(length)
        RAND.nextBytes(salt)
        return Optional.of(Base64.getEncoder().encodeToString(salt)).get()
    }

    private val ITERATIONS = 65536
    private val KEY_LENGTH = 512
    private val ALGORITHM = "PBKDF2WithHmacSHA512"

    fun hashPassword(password: String, salt: String): Optional<String>? {
        val chars = password.toCharArray()
        val bytes = salt.toByteArray()
        val spec = PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH)
        Arrays.fill(chars, Character.MIN_VALUE)
        return try {
            val fac = SecretKeyFactory.getInstance(ALGORITHM)
            val securePassword = fac.generateSecret(spec).encoded
            Optional.of(Base64.getEncoder().encodeToString(securePassword))
        } catch (ex: NoSuchAlgorithmException) {
            System.err.println("Exception encountered in hashPassword()")
            Optional.empty()
        } catch (ex: InvalidKeySpecException) {
            System.err.println("Exception encountered in hashPassword()")
            Optional.empty()
        } finally {
            spec.clearPassword()
        }
    }

    fun verifyPassword(password: String?, key: String?, salt: String?): Boolean {
        val optEncrypted: Optional<String>? = hashPassword(password!!, salt!!)
            return if (!optEncrypted!!.isPresent) false else optEncrypted.get() == key
    }


}