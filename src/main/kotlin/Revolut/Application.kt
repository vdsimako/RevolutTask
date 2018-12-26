package Revolut

import Revolut.controller.AccountController
import Revolut.model.Account
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.json.JavalinJackson
import java.math.BigDecimal

class Application {
    private val javalin: Javalin = Javalin.create().apply {
        contextPath("/revolut")
        defaultContentType("application/json")
//        enableCorsForAllOrigins() //for swagger definition
        port(8080)

        routes {
            path("/accountManagement"){
                get(AccountController::getAllAccounts)
            }
        }
    }

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    init {

        objectMapper.registerKotlinModule()
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)

        JavalinJackson.configure(objectMapper)
    }

    fun start() {

//        val api = OpenAPI()
//            .info(
//                Info()
//                    .title("Test Title!")
//                    .description("Description")
//            )
//            .addTagsItem(
//                Tag()
//                    .name("Name!")
//                    .description("Description!")
//            )

        javalin.get("/greeting") { context -> context.result("Greetings!") }
        javalin.get("/test") { context -> context.json(Account("1", BigDecimal.valueOf(10.0004))) }

        javalin.start()
    }
}

fun main(args: Array<String>) {
//    val app = Application().javalin.start()

//    app.get("/greeting") {context -> context.result("Greetings!") }
//    app.get("/test") {context -> context.json(Account("1", BigDecimal.valueOf(10.0004))) }

    Application().start()
}

