package Revolut.model

import org.jetbrains.exposed.sql.Table

object AccountsDao: Table() {
    val id = integer("account_id").autoIncrement()
    val accountNumber = varchar("account_number", length = 10)
    val amount = decimal(name = "amount", precision = 10 , scale = 4)
}