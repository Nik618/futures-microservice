package com.example.futuresmicroservice.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Obligations", strict = false)
class Obligations {

    @field:ElementList(entry = "Obligation", inline = true)
    lateinit var list: MutableList<Obligation>

}