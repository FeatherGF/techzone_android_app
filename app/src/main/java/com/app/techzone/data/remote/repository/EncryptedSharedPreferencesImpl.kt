package com.app.techzone.data.remote.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

object PreferencesKey {
    const val accessToken = "accessToken"
    const val refreshToken = "refreshToken"
    const val savedCards = "savedCards"
}

class EncryptedSharedPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPreferences = EncryptedSharedPreferences.create(
        "preferences",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getKey(preferencesKey: String): String? {
        return sharedPreferences.getString(preferencesKey, null)
    }
}