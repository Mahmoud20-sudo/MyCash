package com.codeIn.myCash.integration.model.repository.authentication.signUp

import android.util.Log
import com.codeIn.myCash.integration.domain.authentication.signUp.DevicesRepository
import com.codeIn.myCash.integration.domain.authentication.signUp.PackagesRepository
import com.codeIn.myCash.integration.model.dataClasses.authentication.DevicesState
import com.codeIn.myCash.integration.model.dataClasses.authentication.PackagesState
import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.remoteSource.authentication.signUp.AuthGeneralDataRemoteSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthGeneralDataRepository @Inject constructor(private val remoteSource: AuthGeneralDataRemoteSource) :
    PackagesRepository, DevicesRepository {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }
    private val success = NetworkParams.SUCCESS_STATUS

    override suspend fun getPackages(): PackagesState {
        return try {
            val response = remoteSource.getPackages()
            if (response.status == success) response.packages?.let { PackagesState.Success(it) }
                ?: PackagesState.Error("Packages not found")
            else PackagesState.Error(response.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "getPackages: $e ")
            PackagesState.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun getDevices(): DevicesState {
        return try {
            val response = remoteSource.getDevices()
            if (response.status == success) response.devices?.let { DevicesState.Success(it) }
                ?: DevicesState.Error("Devices not found")
            else DevicesState.Error(response.message ?: "An error occurred")
        } catch (e: Exception) {
            Log.e(debugTAG, "getDevices: $e ")
            DevicesState.Error(e.message ?: "An error occurred")
        }
    }
}