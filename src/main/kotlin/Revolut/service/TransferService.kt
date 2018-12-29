package Revolut.service

import Revolut.dblayer.DBLayer
import Revolut.model.dto.TransferRequest
import Revolut.model.dto.TransferResponse

object TransferService {

    fun createTransfer(transferRequest: TransferRequest): TransferResponse {
        val fromAccount = DBLayer.getAccount(transferRequest)

        if (fromAccount?.amount!! < transferRequest.amount) {
            return TransferResponse("Not enough money!!!")
        } else {
            DBLayer.createTransfer(transferRequest)
        }

        return TransferResponse("Success")
    }
}