package com.codeIn.myCash.ui.options.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.DraftsType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DraftsViewModel @Inject constructor() : ViewModel() {


    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    // for testing purposes only (to be removed) TODO: remove this
    private var myDraftsList = arrayListOf(
        Draft(1, "Title 1", "Subtitle 1", "2023-10-25", 1),
        Draft(2, "Title 2", "Subtitle 2", "2023-10-26", 2),
        Draft(3, "Title 3", "Subtitle 3", "2023-10-27", 3),
        Draft(4, "Title 4", "Subtitle 4", "2023-10-28", 4),
        Draft(5, "Title 5", "Subtitle 5", "2023-10-29", 2),
        Draft(6, "Title 6", "Subtitle 6", "2023-10-30", 4),
        Draft(7, "Title 7", "Subtitle 7", "2023-10-31", 3)
    )


    //the draftsType that the user is currently viewing
    private val _draftsType = MutableLiveData<DraftsType>()
    val draftsType: LiveData<DraftsType> = _draftsType

    //the list of the Drafts that the user is currently viewing
    private val _draftsList = MutableLiveData<List<Draft>>()
    val draftsList: LiveData<List<Draft>> = _draftsList


    /**
     * init block to set the default value of the draftsType MutableLiveData to ALL
     */
    init {
        _draftsType.postValue(DraftsType.ALL)
        _draftsList.postValue(myDraftsList)
    }


    /**
     * function to update the value of the draftsType.
     * @param draftsType [DraftsType] the new value of the draftsType, it can be ALL, PRODUCTS, INVOICES or CLIENTS.
     * @return [Unit]
     * @see DraftsType
     */
    fun setDraftsType(draftsType: DraftsType) {
        if (draftsType != _draftsType.value) {
            _draftsType.postValue(draftsType)
            val newList = if (draftsType == DraftsType.ALL) {
                myDraftsList
            } else {
                myDraftsList.filter { it.type == draftsType.value }
            }
            _draftsList.postValue(newList)
        }
    }


    /**
     * function to delete a draft from the drafts list. it takes the draft to be deleted as a parameter and deletes it from the list.
     * @param draft [Draft] the draft to be deleted.
     * @return [Unit]
     */
    fun deleteDraft(draft: Draft) {
        //remove the draft from the list
        myDraftsList = myDraftsList.filter { (it.id != draft.id) } as ArrayList<Draft>

        //filter the list based on the draftsType
        val filteredList = if (_draftsType.value != DraftsType.ALL) {
            myDraftsList.filter { (it.type == draftsType.value?.value) } as ArrayList<Draft>
        } else
            myDraftsList

        //update the UI with the new list
        _draftsList.postValue(filteredList)
    }


    /**
     * function to delete all the drafts from the drafts list. it clears the list.
     * @return [Unit]
     */
    fun deleteAllDrafts() {
        myDraftsList = arrayListOf()
        _draftsList.postValue(myDraftsList)
    }
}