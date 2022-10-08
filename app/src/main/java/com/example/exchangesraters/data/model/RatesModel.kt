package com.example.exchangesraters.data.model
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "ValCurs")
data class RatesModel @JvmOverloads constructor(
    @field:Attribute(name = "ID")
    @param:Attribute(name = "ID")
    var ID: String="",

    @field:Attribute(name = "DateRange1")
    @param:Attribute(name = "DateRange1")
    var DateRange1: String="",

    @field:Attribute(name = "DateRange2")
    @param:Attribute(name = "DateRange2")
    var DateRange2: String="",

    @field:Attribute(name = "name")
    @param:Attribute(name = "name")
    var name: String="",

    @field:ElementList(name = "Record",inline = true)
    @param:ElementList(name = "Record",inline = true)
    var list: List<RecordList>,
)





