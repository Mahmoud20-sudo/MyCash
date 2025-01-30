package com.codeIn.myCash.features.user.domain.edit_profile.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState

interface MyInfoRepository {
    suspend fun myInfo() : ProfileState<UserDTO>
}