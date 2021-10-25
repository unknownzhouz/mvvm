package com.nick.mvvm.io.request

import android.util.ArrayMap
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Callback {

    @GET
    suspend fun get(@Url url: String, @HeaderMap headers: Map<String, String?>? = ArrayMap()):  ResponseBody

    @POST
    suspend fun post(@Url url: String, @Body body: RequestBody?, @HeaderMap headers: Map<String, String?>? = ArrayMap()): ResponseBody

    @POST
    @Multipart
    suspend fun post(@Url url: String, @Part parts: List<MultipartBody.Part>?, @HeaderMap headers: Map<String, String?>? = ArrayMap()): ResponseBody

    @PUT
    suspend fun put(@Url url: String, @Body body: RequestBody?, @HeaderMap headers: Map<String, String?>? = ArrayMap()): ResponseBody

    @DELETE
    suspend fun delete(@Url url: String, @HeaderMap headers: Map<String, String?>? = ArrayMap()): ResponseBody
}