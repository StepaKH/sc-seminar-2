package ru.hse.bank

import ru.hse.bank.dao.FileSystemAccountDao
import ru.hse.bank.entity.AccountEntity


fun main() {

    var accountDao = FileSystemAccountDao()
    val alice = AccountEntity(name = "Alice", sum = 200)
    val bob = AccountEntity("Bob", 3000)
    val kate = AccountEntity("Kate", 99999)



    accountDao.saveAccount(alice)
    accountDao.saveAccount(bob)
    accountDao.saveAccount(kate)

    accountDao.increaseAccountSumByName("Alice", 200)

    accountDao.remittance("Kate", "Bob", 3000)
    accountDao.remittance("Kate", "Alice", 500)
}
