package com.example.exchangesraters.data.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Record", strict = false)
class RecordList @JvmOverloads constructor(

    @field: Element(name = "Nominal", required = false)
    @param:Element(name = "Nominal", required = false)
    var nominal: String="",

    @field: Element(name = "Value", required = false)
    @param:Element(name = "Value", required = false)
    var value: String ="",

    @field: Attribute(name = "Date", required = false)
    @param:Attribute(name = "Date", required = false)
    var date: String="",

    @field: Attribute(name = "Id", required = false)
    @param:Attribute(name = "Id", required = false)
    var id: String="",

    )
