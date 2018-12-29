package Revolut.controller

import Revolut.dblayer.DBLayer
import Revolut.model.AccountDTO
import io.javalin.Context


object AccountController {
    fun getAllAccounts(ctx: Context) {
        ctx.json(DBLayer.getAccountList())
        ctx.status(200)
    }

    fun createAccount(ctx: Context) {
        ctx.json(DBLayer.createAccount(ctx.body<AccountDTO>()))
        ctx.status(201)
    }
}
