package com.example.futuresmicroservice.jpa.repository

import com.example.futuresmicroservice.jpa.entities.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {
    //override fun findById(id: Long): Optional<UserEntity>
}