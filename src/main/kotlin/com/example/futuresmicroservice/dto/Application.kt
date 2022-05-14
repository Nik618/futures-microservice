package com.example.futuresmicroservice.dto

import java.time.LocalDateTime

data class Application (
    var date: LocalDateTime? = null,
    var idUser: Long? = null,
    var count: Long? = null,
    var price: Double? = null,
    var type: String? = null,
    var idObligation: Long? = null
)