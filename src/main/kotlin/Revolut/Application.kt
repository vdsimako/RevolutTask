package Revolut

import Revolut.controller.AccountController
import Revolut.dblayer.DBLayer
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class Application {
    val javalin: Javalin = Javalin.create().apply {
        contextPath("/revolut")
        port(8080)
        enableCaseSensitiveUrls()
    }

    val objectMapper: ObjectMapper = jacksonObjectMapper()

    init {

        objectMapper.registerKotlinModule()
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)

        JavalinJackson.configure(objectMapper)
    }

    fun start() {
        javalin.start()

        DBLayer.createSchema()

        javalin.routes {
            path("/accountManagement") {
                get(AccountController::getAllAccounts)
                post(AccountController::createAccount)
                path("/createTransfer") {
                    post(AccountController::createTransfer)
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    Application().start()
}

