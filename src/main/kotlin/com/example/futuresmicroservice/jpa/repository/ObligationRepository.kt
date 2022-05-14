package com.example.futuresmicroservice.jpa.repository

import com.example.futuresmicroservice.jpa.entities.ObligationEntity
import com.example.futuresmicroservice.jpa.entities.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ObligationRepository : CrudRepository<ObligationEntity, Long> {
    fun findAllByIdBuyer(idBuyer: UserEntity): List<ObligationEntity?>
    fun findAllByIdSeller(idSeller: UserEntity): List<ObligationEntity?>
}