package com.eywa.projectnummi.model.providers

import com.eywa.projectnummi.model.Person

object PeopleProvider {
    val basic = listOf(
            "Person 1",
            "Person 2",
            "Person 3",
    ).mapIndexed { index, name -> Person(index, name) }
}
