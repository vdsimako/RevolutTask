package Revolut.dblayer

import Revolut.model.AccountDTO
import Revolut.model.AccountsDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DBLayer {

    val connection = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver")

    fun getAccountList(): List<AccountDTO> {
        val accounts = ArrayList<AccountDTO>()

        transaction {
            for (account in AccountsDao.selectAll()) {
                account[AccountsDao.id]
                accounts.add(AccountDTO(accountNumber = account[AccountsDao.accountNumber], amount = account[AccountsDao.amount]))
            }
        }

        return accounts
    }

    fun createSchema() {
        transaction {
//            addLogger(StdOutSqlLogger)
            SchemaUtils.create(AccountsDao)

//            commit()
        }
    }

    fun createAccount(account: AccountDTO): AccountDTO {
        transaction {

            AccountsDao.insert {
                it[accountNumber] = account.accountNumber
                it[amount] = account.amount
            }

            commit()
        }
        return account
    }
}