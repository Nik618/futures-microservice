package com.example.futuresmicroservice.jpa.repository

import com.example.futuresmicroservice.jpa.entities.ResultEntity
import com.example.futuresmicroservice.jpa.entities.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : CrudRepository<ResultEntity, Long> {
    fun findAllByIdUser(idUser: UserEntity): List<ResultEntity?>
}