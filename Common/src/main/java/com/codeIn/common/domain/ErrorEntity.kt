package com.codeIn.common.domain

sealed class ErrorEntity {
    object Network : ErrorEntity()

    object NotFound : ErrorEntity()

    object AccessDenied : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    object ServerInternalError : ErrorEntity()

    object Unknown : ErrorEntity()

}