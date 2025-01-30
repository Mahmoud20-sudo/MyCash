package com.codeIn.myCash.integration.model.network

import com.codeIn.myCash.integration.di.app.MyCashPreferences
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkParams @Inject constructor(private val myCashPreferences: MyCashPreferences) {
    companion object {
        const val NAME = "MyCashAppAPI"
        const val BASE_URL = "http://las.emir.life/api/"
        const val SUCCESS_STATUS = 1
        const val ERROR_STATUS = 0
        private const val Content_Type = "application/json"
        private const val Accept = "application/json"
    }

    val headers: Map<String, String> get() = headerMap()

    private fun headerMap(): HashMap<String, String> {
        val header: HashMap<String, String> = HashMap()
        header["Accept"] =
            Accept
        header["Authorization"] =
            myCashPreferences.getAuthToken().toString()
        header["lang"] =
            myCashPreferences.getString(
                MyCashPreferencesKeys.LANG,
                Locale.getDefault().language
            )
        return header
    }

    fun setAuthToken(token: String) {
        myCashPreferences.setAuthToken(token)
    }
}