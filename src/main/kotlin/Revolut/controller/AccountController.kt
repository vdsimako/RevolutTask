package Revolut.controller

import Revolut.model.Account
import io.javalin.Context
import java.math.BigDecimal


object AccountController {
    fun getAllAccounts(ctx: Context) {
        ctx.json(listOf(Account("2", BigDecimal.TEN)))
    }
}
