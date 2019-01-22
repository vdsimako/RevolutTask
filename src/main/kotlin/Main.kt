import io.javalin.Javalin
import io.javalin.RequestLogger
import io.javalin.apibuilder.ApiBuilder
import revolut.controller.AccountController
import revolut.dblayer.DBLayer

fun main(args: Array<String>) {
    JavalinApp(8080).init()
}


class JavalinApp(private val port: Int) {
    fun init(): Javalin {
        val javalinApp = Javalin.create().apply {
            contextPath("/revolut")
            port(port)
            enableCaseSensitiveUrls()
        }.start()

        javalinApp.routes {
            ApiBuilder.path("/accountManagement") {
                ApiBuilder.get(AccountController::getAllAccounts)
                ApiBuilder.post(AccountController::createAccount)
                ApiBuilder.path("/createTransfer") {
                    ApiBuilder.post(AccountController::createTransfer)
                }
            }
        }

        DBLayer.createSchema()

        return javalinApp
    }

}