package com.eywa.projectnummi.model.objects

import com.eywa.projectnummi.database.person.DatabasePerson
import com.eywa.projectnummi.model.HasNameAndId

data class Person(
        val id: Int,
        val name: String,
) : HasNameAndId {
    constructor(dbPerson: DatabasePerson)
            : this(dbPerson.id, dbPerson.name)

    fun asDbPerson() = DatabasePerson(id, name)

    override fun getItemName(): String = name

    override fun getItemId(): Int = id
}
