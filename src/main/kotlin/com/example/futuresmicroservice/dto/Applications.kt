package com.example.futuresmicroservice.dto

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "Applications", strict = false)
class Applications {

    @field:ElementList(entry = "Application", inline = true)
    lateinit var list: MutableList<Application>

}