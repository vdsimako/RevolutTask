package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import model.dao.Accounts
import model.dto.TransferRequest
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

val testTransferRequest: TransferRequest = TransferRequest(
    fromAccountNumber = "1",
    toAccountNumber = "2",
    amount = BigDecimal.TEN
)

val testErrorTransferRequest: TransferRequest = TransferRequest(
    fromAccountNumber = "1",
    toAccountNumber = "2",
    amount = BigDecimal.valueOf(20.0)
)

val testNotFoundTransferRequest: TransferRequest = TransferRequest(
    fromAccountNumber = "3",
    toAccountNumber = "2",
    amount = BigDecimal.valueOf(20.0)
)


inline fun <reified T : Any> String.deserialize(): T =
    jacksonObjectMapper().readValue(this)


fun dropSchema(): Unit {
    transaction {
        //        SchemaUtils.drop(Accounts)
        Accounts.deleteAll()

        model.dao.Accounts.insert {
            it[accountNumber] = "1"
            it[amount] = BigDecimal.TEN
        }

        model.dao.Accounts.insert {
            it[accountNumber] = "2"
            it[amount] = BigDecimal.TEN
        }

        commit()
    }
}