package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity

class RuntimeAccountDao : AccountDao {
    private val accountEntities = mutableListOf<AccountEntity>()

    override fun findAccountByName(name: String): AccountEntity =
        accountEntities.find { it.name == name } ?: throw RuntimeException("Account not found")

    override fun increaseAccountSumByName(name: String, sum: Long): AccountEntity =
        findAccountByName(name).also { it.sum += sum }

    override fun saveAccount(accountEntity: AccountEntity): Boolean = accountEntities.add(accountEntity)

    override fun remittance(nameFrom: String, nameTo: String, sum: Long): Pair<AccountEntity, AccountEntity>{
        return remittancefinal(findAccountByName(nameFrom), findAccountByName(nameTo),sum)
    }

    override fun remittancefinal(accountEntityFrom: AccountEntity, accountEntityTo: AccountEntity, sum: Long): Pair<AccountEntity, AccountEntity> {
        var newAccountFrom = accountEntityFrom
        var newAccountTo = accountEntityTo
        var firstSum = newAccountFrom.sum

        if (newAccountFrom.sum - sum >= 0)
        {
            newAccountFrom = increaseAccountSumByName(accountEntityFrom.name, -sum)
        } else {
            throw Exception("not enough money in the account")
        }

        if (newAccountFrom.sum != sum)
        {
            newAccountTo = increaseAccountSumByName(accountEntityTo.name, sum)
        } else {
            throw Exception("something wrong")
        }

        println("Transaction from ${newAccountFrom.name} -> ${newAccountTo.name} amount ${sum}")

        return Pair(newAccountFrom, newAccountTo)
    }

}
