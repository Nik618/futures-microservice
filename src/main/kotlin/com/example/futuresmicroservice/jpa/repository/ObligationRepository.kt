package com.example.futuresmicroservice.jpa.repository

import com.example.futuresmicroservice.jpa.entities.ObligationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ObligationRepository : CrudRepository<ObligationEntity, Long> {
    fun findAllById(id: Long): List<ObligationEntity?>
}