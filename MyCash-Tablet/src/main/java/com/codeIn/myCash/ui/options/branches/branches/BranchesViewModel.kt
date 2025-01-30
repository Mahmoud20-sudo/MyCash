package com.codeIn.myCash.ui.options.branches.branches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.options.branches.Branch
import com.codeIn.myCash.features.sales.data.branch.model.response.DeleteDTO
import com.codeIn.myCash.features.sales.domain.branch.usecase.DeleteBranchIDUSeCase
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.domain.branch.usecase.BranchesUseCase
import com.codeIn.myCash.features.sales.domain.branch.usecase.DeleteBranchesUseCase
import com.codeIn.myCash.features.sales.domain.branch.usecase.UpdateBranchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BranchesViewModel @Inject constructor(
    private val mBranchesUseCase: BranchesUseCase,
    private val mDeleteBranchesUseCase: DeleteBranchesUseCase,
    private val mDeleteBranchByIDUseCase : DeleteBranchIDUSeCase,
    private val updateBranchUseCase: UpdateBranchUseCase
) : ViewModel() {


    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    private val _branchesList = MutableStateFlow<List<BranchesDTO.Data.Data>>(emptyList())
    val branchesList: StateFlow<List<BranchesDTO.Data.Data>> = _branchesList

    private val _deleteBranch = MutableStateFlow<DeleteDTO?>(null)
    val deleteBranch : StateFlow<DeleteDTO?> = _deleteBranch

    private var _searchText = MutableLiveData<String?>(null)
    val searchText : LiveData<String?> = _searchText



    //init the branches list
    init {
        getBranches()
    }

    fun getBranches() = launchIO {
            try {
                val response = mBranchesUseCase.invoke(searchText.value)
                if (response.isSuccessful) {
                    val branchesDTO = response.body()
                    branchesDTO?.data?.let { data ->
                        _branchesList.value = data.data?.mapNotNull { nestedData ->
                            nestedData
                        } ?: emptyList()
                    }
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }


    fun updateSearchText(query: String) {
        _searchText.postValue(query)
    }


    /**
     * Called to delete all the branches from the list of branches
     * @return [Unit]
     */
    fun deleteAllBranches( all: Int? = null) {
        viewModelScope.launch {
            try {
                val response = mDeleteBranchesUseCase.invoke( all!!)
                if (response.isSuccessful) {
                    _deleteBranch.value = response.body()
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Called to delete a branch from the list of branches
     * @param branch [Branch] the branch to be deleted
     * @return [Unit]
     */
    fun deleteBranches( branchID: Int? = null) {
        viewModelScope.launch {
            try {
                val response = mDeleteBranchByIDUseCase.invoke( branchID!!)
                if (response.isSuccessful) {
                    _deleteBranch.value = response.body()
                    getBranches()
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateBranch( name: String, isMain: Int, address: String, branchID: Int, status: Int,additionalInfo: String,city:String,phone:String?){
        launchIO {
            updateBranchUseCase.invoke(name, isMain, address, branchID, status,additionalInfo, city, phone)
                .let {
                    getBranches()
                }
        }
    }
}