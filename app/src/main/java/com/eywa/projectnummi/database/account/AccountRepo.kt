package com.eywa.projectnummi.database.account

class AccountRepo(private val accountDao: AccountDao) {
    fun get() = accountDao.get()
    suspend fun insert(account: DatabaseAccount) = accountDao.insert(account)
    suspend fun delete(account: DatabaseAccount) = accountDao.delete(account)
    suspend fun update(vararg accounts: DatabaseAccount) = accountDao.update(*accounts)
}
