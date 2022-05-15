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
import java.time.LocalDateTime

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

        val application: Application = gson.fromJson(request, object : TypeToken<Application>() {}.type)
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
        val resultEntities = resultRepository.findAllById(requestIdUser)

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

        val userEntity = userRepository.findById(obligationRequest.idUser!!).get()
        val applicationEntity = applicationRepository.findById(obligationRequest.idApplication!!).get()

        var onDelete = true
        if (obligationRequest.count!! < applicationEntity.count!!)
            onDelete = false

        // если обязательство уже существует на момент продажи
        val obligationEntityLast = applicationEntity.idObligation
        if (obligationEntityLast != null) {
            obligationEntityLast.count = obligationEntityLast.count?.minus(applicationEntity.count!!)
            obligationRepository.save(obligationEntityLast)
        }

        // а это новое обязательство, его всё равно нужно создать
        val obligationEntity = ObligationEntity().apply {
            date = applicationEntity.date
            count = applicationEntity.count
            price = applicationEntity.price
            if (obligationEntityLast != null) { // если мы дробим существующее обязательство
                price = obligationEntityLast.price // цена обязательства не меняется
                resultRepository.save(ResultEntity().apply {
                    value = applicationEntity.price?.minus(obligationEntityLast.price!!)
                    date = LocalDateTime.now()
                    idUser = userEntity
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
            applicationEntity.count = applicationEntity.count!! - obligationEntity.count!!
            applicationRepository.save(applicationEntity)
        }
        return true
    }

    @PostMapping("/api/createUser")
    fun createUser(@RequestBody request : String): Boolean? {

        val user: User = gson.fromJson(request, object : TypeToken<User>() {}.type)
        val userEntity = UserEntity().apply {
            login = user.login
            password = user.password
            role = user.role
            name = user.name
        }
        userRepository.save(userEntity)
        return true
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


}