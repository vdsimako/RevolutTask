package Revolut.dblayer

import Revolut.model.dto.Account
import Revolut.model.dao.Accounts
import Revolut.model.dto.TransferRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

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

    fun createSchema() {
        transaction {
//            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Accounts)

            Accounts.insert {
                it[accountNumber] = "1"
                it[amount] = BigDecimal.TEN
            }

            Accounts.insert {
                it[accountNumber] = "2"
                it[amount] = BigDecimal.TEN
            }

            Accounts.insert {
                it[accountNumber] = "3"
                it[amount] = BigDecimal.TEN
            }

            commit()
        }
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

        transaction {
            val account = Accounts.select { Accounts.accountNumber eq transferRequest.fromAccountNumber }.first()

            accountDto = Account(account[Accounts.accountNumber], account[Accounts.amount])
        }
        return accountDto
    }
}