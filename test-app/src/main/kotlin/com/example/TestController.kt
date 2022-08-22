package com.example

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.HttpClient
import jakarta.inject.Inject
import java.net.URI

val runtimeApiURL = URI("http://localhost:8000")

@Controller("/test")
class TestController {

    @Inject
    lateinit var ctx: ApplicationContext

    @Get("/call")
    @Produces(MediaType.TEXT_PLAIN)
    fun doTest(): String {
        val config = DefaultHttpClientConfiguration()
        config.setReadIdleTimeout(null)
        config.setReadTimeout(null)
        config.setConnectTimeout(null)
        val endpointClient: HttpClient = ctx.createBean(HttpClient::class.java, runtimeApiURL, config)
        val blockingHttpClient: BlockingHttpClient = endpointClient.toBlocking()
        try {
            val response =
                blockingHttpClient.exchange(HttpRequest.GET<String>("/"), Argument.of(String::class.java))
            return response.body()!!
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    @Get("/echo")
    @Produces(MediaType.TEXT_PLAIN)
    fun echo(): String {
        return "ECHO!"
    }
}