package service

import dblayer.DatabaseFactory.dbQuery
import model.dao.Accounts
import model.dto.TransferRequest
import model.dto.TransferStatus
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

object TransferService {

    suspend fun createTransfer(transferRequest: TransferRequest): TransferStatus? {

        return dbQuery {
            val fromAccount = Accounts.select { Accounts.accountNumber eq transferRequest.fromAccountNumber }.firstOrNull()
            var status : TransferStatus? = null


            if (fromAccount != null) {

                if (fromAccount[Accounts.amount] < transferRequest.amount) {
                    status =  TransferStatus.NOT_ENOUGH_MONEY
                } else {
                    Accounts.update({ Accounts.accountNumber eq transferRequest.toAccountNumber }) {
                        with(SqlExpressionBuilder) {
                            it.update(amount, amount + transferRequest.amount)
                        }
                    }

                    Accounts.update({ Accounts.accountNumber eq transferRequest.fromAccountNumber }){
                        with(SqlExpressionBuilder) {
                            it.update(amount, amount - transferRequest.amount)
                        }
                    }

                    status = TransferStatus.SUCCESS
                }
            }

            return@dbQuery status
        }
    }
}