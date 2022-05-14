package com.example.futuresmicroservice.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Users", strict = false)
class Users {

    @field:ElementList(entry = "User", inline = true)
    lateinit var list: MutableList<User>

}