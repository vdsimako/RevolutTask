package Revolut.model

import java.math.BigDecimal

data class AccountDTO(val accountNumber: String, val amount: BigDecimal = BigDecimal.ZERO)