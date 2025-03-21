package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity
import java.nio.charset.Charset
import kotlin.io.path.Path

class FileSystemAccountDao : AccountDao {

    companion object {
        private val DIR_WITH_ACCOUNTS = "C:\\Users\\Stepa\\IdeaProjects\\sc-seminar-2\\src\\main\\kotlin\\ru\\hse\\bank\\dao\\accounts"
    }

    override fun findAccountByName(name: String): AccountEntity {
        val file = Path(DIR_WITH_ACCOUNTS, name).toFile()
        val file_sum : Long

        if (!file.exists()) {
            throw Exception("No accounts with this name")
        }

        val lines = file.bufferedReader().readLines()
        file_sum = lines[0].toLong()
        return AccountEntity(name, file_sum)
    }

    override fun increaseAccountSumByName(name: String, sum: Long): AccountEntity {
        val file = Path(DIR_WITH_ACCOUNTS, name).toFile()
        var file_sum : Long

        if (file.exists()) {
            val lines = file.bufferedReader().readLines()
            file_sum = lines[0].toLong()
            file_sum += sum
            file.delete()
        } else {
            throw Exception("No accounts with this name")
        }

        val isFileCreated : Boolean = file.createNewFile()
        file.writeText("${file_sum}", Charset.defaultCharset())
        return AccountEntity(name, file_sum)
    }

    override fun saveAccount(accountEntity: AccountEntity): Boolean {
        val file = Path(DIR_WITH_ACCOUNTS, accountEntity.name).toFile()

        if (file.exists()) {
            file.delete()
        }

        val isFileCreated : Boolean = file.createNewFile()
        file.writeText("${accountEntity.sum}", Charset.defaultCharset())

        return file.exists()
    }

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

        val logFileFrom = Path(DIR_WITH_ACCOUNTS, accountEntityFrom.name + "Log").toFile()
        val logFileTo = Path(DIR_WITH_ACCOUNTS, accountEntityTo.name + "Log").toFile()
        val allLog = Path(DIR_WITH_ACCOUNTS, "transaction").toFile()

        if (!logFileFrom.exists()) {
            logFileFrom.createNewFile()
        }

        if (!logFileTo.exists()) {
            logFileTo.createNewFile()
        }

        if (!allLog.exists()) {
            allLog.createNewFile()
        }

        logFileFrom.appendText("Send ${sum} to ${newAccountTo.name}\n")
        logFileTo.appendText("Get ${sum} from ${newAccountFrom.name}\n")
        allLog.appendText("Transaction ${sum} ${newAccountFrom.name} ${newAccountTo.name}\n")


        return Pair(newAccountFrom, newAccountTo)
    }
}
