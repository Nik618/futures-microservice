package com.example.futuresmicroservice.jpa.repository

import com.example.futuresmicroservice.jpa.entities.ApplicationEntity
import com.example.futuresmicroservice.jpa.entities.ObligationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, Long> {
    fun findAllByType(type: String): List<ApplicationEntity?>
    fun findByIdObligation(idObligation: ObligationEntity): List<ApplicationEntity?>
}