package controller

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import model.dto.TransferResponse
import model.dto.TransferStatus
import service.AccountService
import service.TransferService


fun Route.transfer() {

    route("/accountManagement") {
        get("/") {
            call.respond(AccountService.getAllAccounts())
        }

        put("/createTransfer") {
            val transfer = TransferService.createTransfer(call.receive())

            when(transfer) {
                TransferStatus.SUCCESS -> call.respond(HttpStatusCode.OK, TransferResponse(transfer.status))

                TransferStatus.NOT_ENOUGH_MONEY -> call.respond(HttpStatusCode.MethodNotAllowed, TransferResponse(transfer.status))

                else -> call.respond(HttpStatusCode.NotFound)
            }
        }

    }


}

