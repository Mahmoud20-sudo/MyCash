package com.codeIn.common.domain

interface ErrorHandler {
    operator fun invoke(code: Int): ErrorEntity

    fun getError(throwable: Throwable): ErrorEntity
}