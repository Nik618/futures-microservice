package com.example.futuresmicroservice.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Results", strict = false)
class Results {

    @field:ElementList(entry = "Result", inline = true)
    lateinit var list: MutableList<Result>

}