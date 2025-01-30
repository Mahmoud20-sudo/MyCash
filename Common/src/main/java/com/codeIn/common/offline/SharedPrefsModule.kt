package com.codeIn.common.offline

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPrefsModule @Inject constructor(@ApplicationContext context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val packageName = context.packageName

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        packageName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getValue(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)


    fun getLanguage(): String {
        return sharedPreferences.getString(Constants.LANG, Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    fun getMode(mode: String = "light"): String {
        return sharedPreferences.getString(Constants.MODE, mode) ?: "light"
    }

    fun putValue(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringFlowForKey(keyForFloat: String) = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForFloat == key) {
                trySend(sharedPreferences.getString(key, "0") ?: "0")
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        if (sharedPreferences.contains(keyForFloat)) {
            send(
                sharedPreferences.getString(keyForFloat, "0f") ?: "0"
            ) // if you want to emit an initial pre-existing value
        }
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED) // so trySend never fails
}