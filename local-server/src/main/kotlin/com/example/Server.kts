package com.example

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket

val port = 8000
val serverSocket = ServerSocket(port)

println("listening port: " + port.toString())

lateinit var s: String
while (true) {
    val clientSocket = serverSocket.accept()

    val `in` = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    val out = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))

    do {
        s = `in`.readLine()
        println(s)
    } while (!s.isEmpty())

    val body = """
        <!DOCTYPE html><html><head><title>Exemple</title></head><body><p>Server exemple.</p></body></html>
    """.trimIndent()

    out.write("HTTP/1.0 200 OK\r\n")
    out.write("Date: Fri, 31 Dec 2017 23:59:59 GMT\r\n")
    out.write("Server: Apache/0.8.4\r\n")
    out.write("Content-Type: text/html\r\n")
    out.write("Content-Length: ${body.toByteArray().size}\r\n")
    out.write("Expires: Sat, 01 Jan 2020 00:59:59 GMT\r\n")
    out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n")
    out.write("\r\n")
    out.write(body)


    out.close()
    `in`.close()
    clientSocket.close()
}