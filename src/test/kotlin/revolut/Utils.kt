package revolut

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.JSONArray
import org.json.JSONObject
import revolut.model.dao.Accounts
import revolut.model.dto.Account
import revolut.model.dto.TransferRequest
import java.math.BigDecimal

val testTransferRequest: TransferRequest = TransferRequest(fromAccountNumber = "1",
    toAccountNumber = "2",
    amount = BigDecimal.TEN)

val testErrorTransferRequest: TransferRequest = TransferRequest(fromAccountNumber = "1",
    toAccountNumber = "2",
    amount = BigDecimal.valueOf(20.0))

val connection = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "org.h2.Driver")

fun parseAccountListFromJSONArray(json: JSONArray): ArrayList<Account> {
    val accountList = ArrayList<Account>()

    for (item in json) {

        accountList.add(Account(accountNumber = (item as JSONObject).getString("accountNumber"),
            amount = item.getBigDecimal("amount")))
    }

    return accountList
}

fun createSchema(): Unit {
    transaction {
        SchemaUtils.create(Accounts)

        revolut.model.dao.Accounts.insert {
            it[accountNumber] = "1"
            it[amount] = BigDecimal.TEN
        }

        revolut.model.dao.Accounts.insert {
            it[accountNumber] = "2"
            it[amount] = BigDecimal.TEN
        }

        commit()
    }
}

fun dropSchema(): Unit {
    transaction {
        SchemaUtils.drop(Accounts)
    }
}