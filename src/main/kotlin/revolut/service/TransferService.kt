package revolut.service

import revolut.dblayer.DBLayer
import revolut.model.dto.TransferRequest
import revolut.model.dto.TransferResponse

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