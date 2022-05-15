package com.example.futuresmicroservice.dto

import java.time.LocalDateTime

data class Obligation (
    var id: Long? = null,
    var date: String? = null,
    var idSeller: Long? = null,
    var idBuyer: Long? = null,
    var count: Long? = null,
    var price: Double? = null
)