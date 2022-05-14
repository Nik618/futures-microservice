package com.example.futuresmicroservice.dto

data class User (
    var login: String? = null,
    var password: String? = null,
    var role: String? = null,
    var name: String? = null
)