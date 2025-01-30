package com.codeIn.common.di

import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val prefs : SharedPrefsModule): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val lang  = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken()) ?: prefs.getValue(Constants.registerToken())
        token?.let { request.addHeader("Authorization", it) }

        request.addHeader("lang", "$lang")
        request.addHeader("Accept","application/json")
        return chain.proceed(request.build())
    }}