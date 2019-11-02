package com.catchman.data.interceptor

import android.content.Context
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.nio.charset.Charset


class LoggingInterceptor : Interceptor {

    private val JSON_INDENT = 2
    private val UTF8 = Charset.forName("UTF-8")


    enum class Level {
        NONE, BASIC, HEADERS, BODY
    }

    interface Logger {
        fun log(message: String)
    }

    private val logger: Logger
    private var context: Context? = null

    @Volatile
    private var level = Level.NONE

    constructor(logger: Logger, context: Context?){
        this.logger = logger
        this.context = context
        //accessToken = Pref.getInstance()?.getAccountUser(context!!)?.access_token?:""
    }

    constructor(){
        logger = object : Logger {
            override fun log(message: String) = Platform.get().log(Platform.INFO, message, null)
        }
        //authToken = String()
    }

    fun setLevel(level: Level): LoggingInterceptor {
        this.level = level
        return this
    }


    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        request = request.newBuilder()
               // .addHeader("Authorization", "Bearer " + Pref.getInstance()?.getAccountUser(context!!))
                .build()


        //if (level == Level.NONE){
        return chain.proceed(request)
        //}
//        if(authToken.isNullOrBlank()){
//            request.newBuilder().addHeader("Authorization", authToken)
//        }
//
//        val message = StringBuilder()
//        val logBody = level == Level.BODY
//        val logHeaders = level == Level.HEADERS
//
//        val requestBody = request.body()
//        val hasRequestBody = requestBody != null
//
//        val connection = chain.connection()
//        var requestStartMessage = "--> ${request.method()} ${request.url()} ${connection?.protocol()}"
//        if (!logHeaders && hasRequestBody){
//            requestStartMessage += " ( ${requestBody?.contentLength()} -byte body)"
//        }
//        message.append(requestStartMessage).append("\n\n")
//        message.append("Authorization", authToken)
//        val headers = request.headers()
//        headers.newBuilder().add("Authorization", "!32142134")
//        if (logHeaders){
//            if (hasRequestBody){
//                if (requestBody?.contentType() != null){
//                    message.append("Content-Type: ${requestBody.contentType()}\n")
//                }
//                if (requestBody?.contentLength() != -1L){
//                    message.append("Content-Length: ${requestBody?.contentLength()}\n")
//                }
//            }
//
//
//            for (i in 0 until headers.size()){
//                val name = headers.name(i)
//                if (!name.equals("Content-Type", true) && !name.equals("Content-Length", true)){
//                    message.append("$name: ${headers.value(i)}\n")
//                }
//            }
//            if (!logBody || !hasRequestBody){
//                message.append("--> END ${request.method()}\n")
//            } else if (bodyEncoded(request.headers())){
//                message.append("--> END ${request.method()} (encoded body omitted)\n")
//            } else {
//                val buffer = Buffer()
//                requestBody?.writeTo(buffer)
//
//                val charset = requestBody?.contentType()?.charset(UTF8) ?: UTF8
//
//                message.append('\n')
//                if (isPlaintext(buffer)){
//                    val body = formatJson(buffer.clone().readString(charset))
//                    message.append("$body\n\n--> END ${request.method()} (${requestBody?.contentLength()}-byte body)\n")
//                } else {
//                    message.append("--> END ${request.method()} (binary ${requestBody?.contentLength()}-byte body omitted)\n")
//                }
//            }
//        }

//        logger.log(message.toString())
//        message.setLength(0)
//
//        val startNs = System.nanoTime()
//        var response: Response
//        try {
//            response = chain.proceed(request)
//        } catch (e: IOException){
//            message.append("<-- HTTP FAILED:\n$e\n")
//            logger.log(message.toString())
//            throw e
//        }
//        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
//
//        val responseBody = response.body()
//        val contentLength = responseBody?.contentLength() ?: -1L
//        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
//        message.append("<-- ${response.code()} ${response.message()} ${response.request().url()} ($tookMs ms")
//        if (!logHeaders){
//            message.append(", $bodySize body")
//        }
//        message.append(")\n\n")
//
//        if (logHeaders){
//            val headers = response.headers()
//            for (i in 0 until headers.size()){
//                message.append("${headers.name(i)}: ${headers.value(i)}\n")
//            }
//
//            if (!logBody || !HttpHeaders.hasBody(response)){
//                message.append("<-- END HTTP\n")
//            } else if (bodyEncoded(response.headers())){
//                message.append("<-- END HTTP (encoded body omitted)\n")
//            } else {
//                val source = responseBody?.source()
//                source?.request(Long.MAX_VALUE)
//                val buffer = source?.buffer()
//
//                val charset = responseBody?.contentType()?.charset(UTF8) ?: UTF8
//
//                if (buffer != null && !isPlaintext(buffer)){
//                    message.append("\n<-- END HTTP (binary ${buffer.size()}-byte body omitted)\n")
//                    logger.log(message.toString())
//                    return response
//                }
//
//                if (contentLength != 0L){
//                    val body = formatJson(buffer?.clone()?.readString(charset))
//                    message.append("\n$body\n\n")
//                }
//
//                message.append("<-- END HTTP (${buffer?.size()}-byte body\n")
//            }
//            logger.log(message.toString())
//        }
//
//        return response
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 1..16) {
                if (prefix.exhausted()) break
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", true)
    }

    private fun formatJson(json: String?): String {
        if (json == null || json.isEmpty()) {
            return "Empty/Null json content"
        }
        return try {
            json.apply {
                trim()
                if (json.startsWith("{")) {
                    JSONObject(this).toString(JSON_INDENT)
                } else if (json.startsWith("[")) {
                    JSONArray(this).toString(JSON_INDENT)
                }
            }
        } catch (e: JSONException) {
            json
        }
    }
}