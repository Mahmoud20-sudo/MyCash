package com.codeIn.common.base_paging_source

import androidx.paging.PagingSource
import retrofit2.Response


abstract class BasePagingSource<key : Any, value : Any> : PagingSource<key, value>() {

    fun getNotSuccessfulError(response: Response<*>): LoadResult<key, value>? {
        return when {
            response.body() == 401 -> LoadResult.Error(getUnAuthorizedException(response))
            else -> null
        }
    }

    private fun getUnAuthorizedException(response: Response<*>) =
        Exception("Api request to url: ${response.raw().request.url}: failed with code ${response.code()} + $response")

}