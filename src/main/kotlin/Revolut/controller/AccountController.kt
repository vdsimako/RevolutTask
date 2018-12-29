package Revolut.controller

import Revolut.dblayer.DBLayer
import Revolut.model.dto.Account
import Revolut.model.dto.TransferRequest
import Revolut.service.TransferService
import io.javalin.Context
import java.math.BigDecimal


object AccountController {
    fun getAllAccounts(ctx: Context) {
        ctx.json(DBLayer.getAccountList())
        ctx.status(200)
    }

    fun createAccount(ctx: Context) {
        ctx.json(DBLayer.createAccount(ctx.body<Account>()))
        ctx.status(201)
    }

    fun createTransfer(ctx: Context) {
        ctx.json(TransferService.createTransfer(ctx.body<TransferRequest>()))
        ctx.status(202)
    }

    fun getTestTransfer(ctx: Context) {
        ctx.json(TransferRequest("1", "2", BigDecimal.TEN))
    }
}
