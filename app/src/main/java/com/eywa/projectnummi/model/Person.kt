package com.eywa.projectnummi.model

import com.eywa.projectnummi.database.person.DatabasePerson

data class Person(
        val id: Int,
        val name: String,
) : NamedItem {
    constructor(dbPerson: DatabasePerson)
            : this(dbPerson.id, dbPerson.name)

    fun asDbPerson() = DatabasePerson(id, name)

    override fun getItemName(): String = name
}
