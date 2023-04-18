package com.modatta.plugins.facetec

import com.facetec.sdk.FaceTecSDK
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit

val HttpClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(180, TimeUnit.SECONDS)
        .connectTimeout(180, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(180, TimeUnit.SECONDS)
        .build()

fun OkHttpClient.enqueuePostRequest(
    url: URI,
    payload: JSONObject,
    token: String = "",
    deviceKeyIdentifier: String,
    onResponse: (content: JSONObject) -> Unit,
    onFailure: (exception: Exception) -> Unit,
    onProgress: ((percentage: Float) -> Unit)? = null
) {
    val baseBody = RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"),
        payload.toString()
    )




    val request = Request.Builder()
        .url(url.toURL())
        .header("X-Device-Key", deviceKeyIdentifier)
        .header("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(token))
        .post(baseBody)
        .build()

    newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e)
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val body = response.body()?.string()

                response.body()?.close()

                if (body == null) {
                    throw Exception("Response body is null")
                }

                onResponse(JSONObject(body))
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }

    })
}
fun OkHttpClient.enqueueGetRequest(
    url: URI,
    token: String = "",
    deviceKeyIdentifier: String,
    onResponse: (content: JSONObject) -> Unit,
    onFailure: (exception: Exception) -> Unit,
    onProgress: ((percentage: Float) -> Unit)? = null
) {


    val request = Request.Builder()
        .url(url.toURL())
        .header("X-Device-Key", deviceKeyIdentifier)
        .header("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(token))
        .get()
        .build()

    newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e)
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                val body = response.body()?.string()

                response.body()?.close()

                if (body == null) {
                    throw Exception("Response body is null")
                }

                onResponse(JSONObject(body))
            } catch (exception: Exception) {
                onFailure(exception)
            }
        }

    })
}




