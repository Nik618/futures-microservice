package com.example.futuresmicroservice.dto

import java.time.LocalDateTime

data class Obligation (
    var date: LocalDateTime? = null,
    var idSeller: Long? = null,
    var idBuyer: Long? = null,
    var count: Long? = null,
    var price: Double? = null
)