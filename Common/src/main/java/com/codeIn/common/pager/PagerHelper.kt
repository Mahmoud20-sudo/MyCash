package com.codeIn.common.pager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeIn.common.data.Pagination


/**
 * @param _isLoading [MutableLiveData] to store the loading state of the pager, Optional, pass it if you want to update it from the outside or start if with specific value.
 * @param nextPage [Int] to store the next page number, Optional, pass it if you want to start with specific page.
 * @param pagination [Pagination] to store the pagination data, Optional, pass it if you want to start with specific pagination.
 */
class PagerHelper(
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false),
    private var nextPage: Int = 1,
    private var pagination: Pagination = Pagination()
) {

    /**
     * @return [LiveData] to be observed to get the loading state of the pager
     */
    val isLoading: LiveData<Boolean> = _isLoading

    val total get() = pagination.total

    /**
     * checks if we should fetch the next page and if so, fetches it and updates the pagination data
     *
     * @param block [block] to be executed when the pager is ready to fetch the next page, it takes two parameters, the next page number and a function to update the pagination data.
     *
     * @return [Boolean] true if the pager should fetch the next page, false otherwise
     */
    fun getNext(block: (Int, (Pagination?) -> Unit) -> Unit): Boolean {
        if (shouldFetchNextPage()) {
            nextPage += 1
            _isLoading.postValue(true)
            block.invoke(nextPage, ::updatePagination)
            return true
        }
        return false
    }

    /**
     * updates the pagination data and the loading state of the pager
     *
     * @param pagination [Pagination] the new pagination data to be set
     * @param nextPage [Int] the new next page number to be set, used if you want to force setting the next page from outside,
     */
    fun updatePagination(pagination: Pagination?, nextPage: Int = this.nextPage) {
        pagination?.let { this.pagination = it }
        this.nextPage = nextPage
        if (_isLoading.value == true)
            _isLoading.postValue(false)
    }

    fun shouldFetchNextPage() = _isLoading.value != true && nextPage < (pagination.totalPages ?: 1)

    /**
     * Called when you want to reset the pager to it's initial state, resets the next page number and the pagination data.
     */
    fun reset() {
        _isLoading.postValue(false)
        nextPage = 0
        pagination = Pagination()
    }

    fun resetWithPageOne() {
        _isLoading.postValue(false)
        nextPage = 1
        pagination = Pagination()
    }


    fun getNextPage() = nextPage
}