package com.codeIn.common.data

import com.codeIn.common.domain.ErrorEntity

sealed class ApiResult<out T> {

    data class Success<out T>(val data: T) : ApiResult<T>()

    data class Error (val errorMessage : String?) : ApiResult<Nothing>()

    object NetworkError/*<T>(val error: ErrorEntity)*/ : ApiResult<Nothing>()
}



