package com.example.futuresmicroservice.dto

import java.time.LocalDateTime

data class Result (
    var date: String? = null,
    var idUser: Long? = null,
    var value: Double? = null,
    var currentPrice: Double? = null,
    var lastPrice: Double? = null,
    var count: Long? = null
)