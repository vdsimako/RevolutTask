package revolut.controller

import revolut.dblayer.DBLayer
import revolut.model.dto.Account
import revolut.model.dto.TransferRequest
import revolut.service.TransferService
import io.javalin.Context
import java.math.BigDecimal


object AccountController {
    fun getAllAccounts(ctx: Context) {
        ctx.json(DBLayer.getAccountList())
        ctx.status(200)
    }

    fun createAccount(ctx: Context) {
        ctx.json(DBLayer.createAccount(ctx.body<Account>()))
        ctx.status(200)
    }

    fun createTransfer(ctx: Context) {
        ctx.json(TransferService.createTransfer(ctx.body<TransferRequest>()))

        ctx.status(200)
    }
}
