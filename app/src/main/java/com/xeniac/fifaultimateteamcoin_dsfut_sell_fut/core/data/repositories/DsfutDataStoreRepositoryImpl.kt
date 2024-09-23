package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.DsfutDataStoreQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.Platform
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.DsfutDataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DsfutDataStoreRepositoryImpl @Inject constructor(
    @DsfutDataStoreQualifier private val dataStore: DataStore<Preferences>
) : DsfutDataStoreRepository {

    private object PreferencesKeys {
        val PARTNER_ID = stringPreferencesKey(name = "partnerId")
        val SECRET_KEY = stringPreferencesKey(name = "secretKey")
        val SELECTED_PLATFORM = stringPreferencesKey(name = "selectedPlatform")
    }

    override fun getPartnerId(): Flow<String?> = dataStore.data.map {
        it[PreferencesKeys.PARTNER_ID]
    }.catch { e ->
        Timber.e("Get partner id failed:")
        e.printStackTrace()
    }

    override fun getSecretKey(): Flow<String?> = dataStore.data.map {
        it[PreferencesKeys.SECRET_KEY]
    }.catch { e ->
        Timber.e("Get secret key failed:")
        e.printStackTrace()
    }

    override fun getSelectedPlatform(): Flow<Platform> = dataStore.data.map {
        val selectedPlatform = it[PreferencesKeys.SELECTED_PLATFORM]

        Platform.entries.find { platform ->
            platform.value == selectedPlatform
        } ?: Platform.CONSOLE
    }.catch { e ->
        Timber.e("Get selected platform failed:")
        e.printStackTrace()
    }

    override suspend fun storePartnerId(partnerId: String?) {
        try {
            dataStore.edit {
                if (partnerId.isNullOrBlank()) {
                    it.remove(PreferencesKeys.PARTNER_ID)
                    Timber.i("Partner id removed")
                } else {
                    it[PreferencesKeys.PARTNER_ID] = partnerId
                    Timber.i("Partner id edited to $partnerId")
                }
            }
        } catch (e: Exception) {
            Timber.e("Store partner id failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSecretKey(secretKey: String?) {
        try {
            dataStore.edit {
                if (secretKey.isNullOrBlank()) {
                    it.remove(PreferencesKeys.SECRET_KEY)
                    Timber.i("Secret key removed")
                } else {
                    it[PreferencesKeys.SECRET_KEY] = secretKey
                    Timber.i("Secret key edited to $secretKey")
                }
            }
        } catch (e: Exception) {
            Timber.e("Store secret key failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSelectedPlatform(platform: Platform) {
        try {
            dataStore.edit {
                it[PreferencesKeys.SELECTED_PLATFORM] = platform.value
                Timber.i("Selected platform edited to ${platform.value}")
            }
        } catch (e: Exception) {
            Timber.e("Store selected platform failed:")
            e.printStackTrace()
        }
    }
}