package com.eywa.projectnummi.database.person

class PersonRepo(private val personDao: PersonDao) {
    fun get() = personDao.get()
    suspend fun insert(person: DatabasePerson) = personDao.insert(person)
    suspend fun delete(person: DatabasePerson) = personDao.delete(person)
    suspend fun update(vararg people: DatabasePerson) = personDao.update(*people)
}
