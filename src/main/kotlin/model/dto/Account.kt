package model.dto

import java.math.BigDecimal

data class Account(val accountNumber: String,
                   val amount: BigDecimal = BigDecimal.ZERO)