package service

import dblayer.DatabaseFactory
import model.dao.Accounts
import model.dto.Account
import org.jetbrains.exposed.sql.selectAll

object AccountService {

    suspend fun getAllAccounts(): List<Account> {
        val accounts = ArrayList<Account>()

        DatabaseFactory.dbQuery {
            for (account in model.dao.Accounts.selectAll()) {
                account[Accounts.id]
                accounts.add(
                    Account(
                        accountNumber = account[Accounts.accountNumber],
                        amount = account[Accounts.amount]
                    )
                )
            }
        }

        return accounts
    }
}