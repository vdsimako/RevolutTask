package revolut

import JavalinApp
import io.javalin.Javalin
import khttp.get
import khttp.post
import org.json.JSONObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import revolut.model.dto.Account
import revolut.model.dto.TransferResponse
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest {

    private val port = 8080
    private val app: Javalin = JavalinApp(port).init()

    private val URL = "http://localhost:" + port
    private val BASE_PATH = "/revolut"
    private val MANAGEMENT_PATH = "/accountManagement"
    private val TRANSFER_PATH = "/createTransfer"

    private val POST_SUCCESS_CODE = 200
    private val SUCCESS_MESSAGE = "Success"
    private val ERROR_MESSAGE = "Not enough money!!!"


    @BeforeEach
    fun init() {
        createSchema()
    }

    @AfterEach
    fun clear() {
        dropSchema()
    }

    @DisplayName("Test check success transfer from one account to another")
    @Test
    fun testTransferComplete() {
        var response = post(
            url = URL + BASE_PATH + MANAGEMENT_PATH + TRANSFER_PATH,
            data = JSONObject(testTransferRequest)
        )


        val transferResponse = response.text.deserialize<TransferResponse>()

        assertEquals(POST_SUCCESS_CODE, response.statusCode)

        assertEquals(TransferResponse(message = SUCCESS_MESSAGE), transferResponse)


        response = get(url = URL + BASE_PATH + MANAGEMENT_PATH)

        val accountList = parseAccountListFromJSONArray(response.jsonArray)

        assertEquals(Account(accountNumber = "1", amount = BigDecimal.valueOf(0.0)),
            accountList.first { account -> account.accountNumber == "1" })

        assertEquals(Account(accountNumber = "2", amount = BigDecimal.valueOf(20.0)),
            accountList.first { account -> account.accountNumber == "2" })

    }

    @Test
    fun testTransferError() {
        var response = post(
            url = URL + BASE_PATH + MANAGEMENT_PATH + TRANSFER_PATH,
            data = JSONObject(testErrorTransferRequest)
        )

        val transferResponse = response.text.deserialize<TransferResponse>()

        assertEquals(POST_SUCCESS_CODE, response.statusCode)
        assertEquals(TransferResponse(message = ERROR_MESSAGE), transferResponse)

        response = get(url = URL + BASE_PATH + MANAGEMENT_PATH)

        val accountList = parseAccountListFromJSONArray(response.jsonArray)

        assertEquals(Account(accountNumber = "1", amount = BigDecimal.valueOf(10.0)),
            accountList.first { account -> account.accountNumber == "1" })

        assertEquals(Account(accountNumber = "2", amount = BigDecimal.valueOf(10.0)),
            accountList.first { account -> account.accountNumber == "2" })
    }
}