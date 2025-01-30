package com.codeIn.common.data

import com.codeIn.common.domain.ErrorEntity
import com.codeIn.common.domain.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when(throwable.code()) {

                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

                    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ServerInternalError

                    else -> ErrorEntity.Unknown
                }
            }
            else -> ErrorEntity.Unknown
        }
    }

    override fun invoke(code: Int): ErrorEntity {
        return when (code) {

            HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

            HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

            HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

            HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ServerInternalError

            else -> ErrorEntity.Unknown
        }
    }
}