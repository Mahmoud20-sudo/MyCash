package com.codeIn.myCash.integration.di.app

import android.content.Context
import androidx.annotation.StringDef
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys.SettingsKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object MyCashPreferencesKeys {
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(LANG, QUICK_INVOICE_MODE, MODE)
    annotation class SettingsKey


    const val LANG = "lang"
    const val MODE = "mode"
    const val QUICK_INVOICE_MODE = "quick_invoice_mode"
}

@Singleton
class MyCashPreferences @Inject constructor(@ApplicationContext appContext: Context) {
    private val AUTH_TOKEN = "auth_token"
    private val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val packageName = appContext.packageName
    private val sharedPreferences = EncryptedSharedPreferences.create(
        appContext,
        packageName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor = sharedPreferences.edit()

    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    fun setAuthToken(token: String) {
        editor.putString(AUTH_TOKEN, "Bearer $token").apply()
    }

    fun getString(@SettingsKey key: String, defaultValue: String = ""): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

    fun getBoolean(@SettingsKey key: String, defaultValue: Boolean = false): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    fun getInt(@SettingsKey key: String, defaultValue: Int = 0): Int =
        sharedPreferences.getInt(key, defaultValue)

    fun getLong(@SettingsKey key: String, defaultValue: Long = 0): Long =
        sharedPreferences.getLong(key, defaultValue)

    fun getFloat(@SettingsKey key: String, defaultValue: Float = 0f): Float =
        sharedPreferences.getFloat(key, defaultValue)

    fun putString(@SettingsKey key: String, value: String?) = editor.putString(key, value).apply()

    fun putBoolean(@SettingsKey key: String, value: Boolean) =
        editor.putBoolean(key, value).apply()

    fun putInt(@SettingsKey key: String, value: Int) =
        editor.putInt(key, value).apply()

    fun putLong(@SettingsKey key: String, value: Long) =
        editor.putLong(key, value).apply()

    fun putFloat(@SettingsKey key: String, value: Float) =
        editor.putFloat(key, value).apply()

    fun remove(@SettingsKey key: String) = editor.remove(key).apply()

    fun clear() = editor.clear().apply()

    fun contains(@SettingsKey key: String): Boolean = sharedPreferences.contains(key)

    fun getAll(): Map<String, *> = sharedPreferences.all
}
