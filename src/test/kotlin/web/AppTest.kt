package web

import common.ServerTest
import io.restassured.RestAssured.get
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import model.dto.TransferResponse
import model.dto.TransferStatus
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.testErrorTransferRequest
import util.testNotFoundTransferRequest
import util.testTransferRequest

class AppTest : ServerTest() {

    @Test
    fun testTransferComplete() {

        val transferResponse = given()
            .contentType(ContentType.JSON)
            .body(testTransferRequest)
            .When().put("/createTransfer").then().statusCode(200).extract().to<TransferResponse>()

        assertEquals(TransferResponse(message = TransferStatus.SUCCESS.status), transferResponse)

        val accountList = get().then().statusCode(200)

        accountList.body("find{it.accountNumber == '1'}.amount", `is`(0.0f))
        accountList.body("find{it.accountNumber == '2'}.amount", `is`(20.0f))

    }

    @Test
    fun testTransferError() {
        val transferResponse = given()
            .contentType(ContentType.JSON)
            .body(testErrorTransferRequest)
            .When().put("/createTransfer").then().statusCode(405).extract().to<TransferResponse>()

        assertEquals(TransferResponse(message = TransferStatus.NOT_ENOUGH_MONEY.status), transferResponse)

        val accountList = get().then().statusCode(200)

        accountList.body("find{it.accountNumber == '1'}.amount", `is`(10.0f))
        accountList.body("find{it.accountNumber == '2'}.amount", `is`(10.0f))
    }

    @Test
    fun testTransfreAccountNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body(testNotFoundTransferRequest)
            .When().put("/createTransfer").then().statusCode(404)

        val accountList = get().then().statusCode(200)

        accountList.body(
            "find{it.accountNumber == '${testNotFoundTransferRequest.fromAccountNumber}'}",
            `is`(nullValue())
        )
        accountList.body("find{it.accountNumber == '2'}.amount", `is`(10.0f))
    }
}
