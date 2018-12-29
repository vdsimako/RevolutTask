package Revolut.model.dao

import org.jetbrains.exposed.sql.Table

object Accounts: Table() {
    val id = integer("account_id").autoIncrement()
    val accountNumber = varchar("account_number", length = 10).uniqueIndex()
    val amount = decimal(name = "amount", precision = 10 , scale = 4)
}