package com.example.futuresmicroservice.dto

import java.time.LocalDateTime

data class Result (
    var date: LocalDateTime? = null,
    var idUser: Long? = null,
    var value: Double? = null
)