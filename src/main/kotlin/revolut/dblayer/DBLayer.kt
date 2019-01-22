package revolut.dblayer

import revolut.model.dto.Account
import revolut.model.dao.Accounts
import revolut.model.dto.TransferRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.sql.Connection

object DBLayer {

    val connection = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver")

    fun getAccountList(): List<Account> {
        val accounts = ArrayList<Account>()

        transaction {
            for (account in Accounts.selectAll()) {
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

    fun createAccount(account: Account): Account {
        transaction {

            Accounts.insert {
                it[accountNumber] = account.accountNumber
                it[amount] = account.amount
            }

            commit()
        }
        return account
    }

    fun createTransfer(transferRequest: TransferRequest) {
        transaction {
            Accounts.update ({ Accounts.accountNumber eq transferRequest.toAccountNumber }) {
                with(SqlExpressionBuilder) {
                    it.update(Accounts.amount, Accounts.amount + transferRequest.amount)
                }
            }

            Accounts.update ({ Accounts.accountNumber eq transferRequest.fromAccountNumber }){
                with(SqlExpressionBuilder) {
                    it.update(Accounts.amount, Accounts.amount - transferRequest.amount)
                }
            }

            commit()
        }
    }

    fun getAccount(transferRequest: TransferRequest): Account? {
        var accountDto: Account? = null

        transaction(Connection.TRANSACTION_READ_COMMITTED, 2) {
            val account = Accounts.select { Accounts.accountNumber eq transferRequest.fromAccountNumber }.first()

            accountDto = Account(account[Accounts.accountNumber], account[Accounts.amount])
        }
        return accountDto
    }

    fun createSchema() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Accounts)
        }
    }
}