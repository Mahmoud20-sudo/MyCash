package com.codeIn.myCash.features.sales.data.clients.repository

import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.clients.remote.Client
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.repository.ClientsRepository
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateVendorValidationUseCase
import javax.inject.Inject

class ClientsRepositoryImpl @Inject constructor(
    private var client: Client,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl,
    private val createClientValidationUseCase: CreateClientValidationUseCase,
    private val createVendorValidationUseCase: CreateVendorValidationUseCase
) : ClientsRepository {
    override suspend fun getClients(
        phone: String?,
        searchText: String?,
        paymentStatus: String?,
        type: String?,
        limit: String?
    ): ClientState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            client.getClients(lang, token, phone, searchText, paymentStatus, type, limit)
                .let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            ClientState.SuccessShowClients(response.body()?.data)
                        } else {
                            ClientState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401) {
                        ClientState.UnAuthorized
                    } else {
                        ClientState.ServerError(errorHandler.invoke(response.code()))
                    }
                }

        } catch (throwable: Throwable) {
            ClientState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleClient(clientId: String?, type: String?): ClientState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            client.getSingleClient(lang, token, clientId, type).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {

                        ClientState.SuccessShowSingleClient(response.body()?.data)
                    } else {
                        ClientState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    ClientState.UnAuthorized
                } else {
                    ClientState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ClientState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun deleteClient(clientId: String?, type: String?): ClientState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            client.deleteClient(lang, token, clientId, type).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {

                        ClientState.SuccessDeleteClient(response.body()?.message)
                    } else {
                        ClientState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    ClientState.UnAuthorized
                } else {
                    ClientState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ClientState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun createClient(request: AddClientRequest): ClientState {
        return try {

            if (request.type == ClientsSection.CLIENTS.value) {
                createClientValidationUseCase(request)
                    .let {
                        if (it != InvalidInput.NONE) return ClientState.InputError(it)
                    }
            } else if (request.type == ClientsSection.VENDORS.value) {
                createVendorValidationUseCase(
                    request.name,
                    request.phone,
                    request.taxNo,
                    request.commercialRecordNo,
                    request.email
                )
                    .let {
                        if (it != InvalidInput.NONE) return ClientState.InputError(it)
                    }
            }


            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            client.createClient(
                lang = lang,
                authorization = token,
                type = request.type,
                name = request.name,
                phone = request.phone,
                countryId = request.countryId,
                taxRecord = request.taxNo,
                commercialRecord = request.commercialRecordNo,
                notes = request.extra,
                address = request.address,
                email = request.email
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {

                        ClientState.SuccessShowSingleClient(response.body()?.data)
                    } else {
                        ClientState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    ClientState.UnAuthorized
                } else {
                    ClientState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ClientState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun updateClient(
        request: AddClientRequest
    ): ClientState {

        return try {
            if (request.type == ClientsSection.CLIENTS.value) {
                createClientValidationUseCase.invoke(request)
                    .let {
                        if (it != InvalidInput.NONE) return ClientState.InputError(it)
                    }
            } else if (request.type == ClientsSection.VENDORS.value) {
                createVendorValidationUseCase.invoke(
                    request.name,
                    request.phone,
                    request.taxNo,
                    request.commercialRecordNo,
                    request.email
                )
                    .let {
                        if (it != InvalidInput.NONE) return ClientState.InputError(it)
                    }
            }

            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            client.updateClient(
                lang,
                token,
                clientId = request.clientId,
                name = request.name,
                phone = request.phone,
                countryId = request.countryId,
                taxRecord = request.taxNo,
                commercialRecord = request.commercialRecordNo,
                notes = request.extra,
                address = request.address,
                email = request.email,
                type = request.type
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {

                        ClientState.SuccessShowSingleClient(response.body()?.data)
                    } else {
                        ClientState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    ClientState.UnAuthorized
                } else {
                    ClientState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        } catch (throwable: Throwable) {
            ClientState.ServerError(errorHandler.getError(throwable))
        }
    }

}