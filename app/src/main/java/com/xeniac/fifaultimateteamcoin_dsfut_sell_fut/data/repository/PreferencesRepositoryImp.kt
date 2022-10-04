package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.domain.repository.PreferencesRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_COUNTRY_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_IS_LOGGED_IN_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_LANGUAGE_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_PREVIOUS_REQUEST_TIME_IN_MILLIS_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_RATE_APP_DIALOG_CHOICE_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.DATASTORE_THEME_KEY
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.LOCALE_COUNTRY_UNITED_STATES
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.utils.Constants.LOCALE_LANGUAGE_ENGLISH
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class PreferencesRepositoryImp @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val IS_USER_LOGGED_IN = booleanPreferencesKey(DATASTORE_IS_LOGGED_IN_KEY)
        val CURRENT_APP_THEME = intPreferencesKey(DATASTORE_THEME_KEY)
        val CURRENT_APP_LANGUAGE = stringPreferencesKey(DATASTORE_LANGUAGE_KEY)
        val CURRENT_APP_COUNTRY = stringPreferencesKey(DATASTORE_COUNTRY_KEY)
        val RATE_APP_DIALOG_CHOICE = intPreferencesKey(DATASTORE_RATE_APP_DIALOG_CHOICE_KEY)
        val PREVIOUS_REQUEST_TIME_IN_MILLIS = longPreferencesKey(
            DATASTORE_PREVIOUS_REQUEST_TIME_IN_MILLIS_KEY
        )
    }

    override suspend fun isUserLoggedIn(): Boolean = try {
        settingsDataStore.data.first()[PreferencesKeys.IS_USER_LOGGED_IN] ?: false
    } catch (e: Exception) {
        Timber.e("isUserLoggedIn Exception: $e")
        false
    }

    override suspend fun getCurrentAppTheme(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.CURRENT_APP_THEME] ?: 0
    } catch (e: Exception) {
        Timber.e("getCurrentAppTheme Exception: $e")
        0
    }

    override suspend fun getCurrentAppLanguage(): String = try {
        settingsDataStore.data
            .first()[PreferencesKeys.CURRENT_APP_LANGUAGE] ?: LOCALE_LANGUAGE_ENGLISH
    } catch (e: Exception) {
        Timber.e("getCurrentAppLanguage Exception: $e")
        LOCALE_LANGUAGE_ENGLISH
    }

    override suspend fun getCurrentAppCountry(): String = try {
        settingsDataStore.data
            .first()[PreferencesKeys.CURRENT_APP_COUNTRY] ?: LOCALE_COUNTRY_UNITED_STATES
    } catch (e: Exception) {
        Timber.e("getCurrentAppCountry Exception: $e")
        LOCALE_COUNTRY_UNITED_STATES
    }

    override suspend fun getRateAppDialogChoice(): Int = try {
        settingsDataStore.data.first()[PreferencesKeys.RATE_APP_DIALOG_CHOICE] ?: 0
    } catch (e: Exception) {
        Timber.e("getRateAppDialogChoice Exception: $e")
        0
    }

    override suspend fun getPreviousRequestTimeInMillis(): Long = try {
        settingsDataStore.data.first()[PreferencesKeys.PREVIOUS_REQUEST_TIME_IN_MILLIS] ?: 0L
    } catch (e: Exception) {
        Timber.e("getPreviousRequestTimeInMillis Exception: $e")
        0L
    }

    override suspend fun isUserLoggedIn(value: Boolean) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.IS_USER_LOGGED_IN] = value
                Timber.i("isUserLoggedIn edited to $value")
            }
        } catch (e: Exception) {
            Timber.e("isUserLoggedIn Exception: $e")
        }
    }

    override suspend fun setCurrentAppTheme(index: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_THEME] = index
                Timber.i("AppTheme edited to $index")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppTheme Exception: $e")
        }
    }

    override suspend fun setCurrentAppLanguage(language: String) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_LANGUAGE] = language
                Timber.i("AppLanguage edited to $language")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppLanguage Exception: $e")
        }
    }

    override suspend fun setCurrentAppCountry(country: String) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.CURRENT_APP_COUNTRY] = country
                Timber.i("AppCountry edited to $country")
            }
        } catch (e: Exception) {
            Timber.e("setCurrentAppCountry Exception: $e")
        }
    }

    override suspend fun setRateAppDialogChoice(value: Int) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.RATE_APP_DIALOG_CHOICE] = value
                Timber.i("RateAppDialogChoice edited to $value")
            }
        } catch (e: Exception) {
            Timber.e("setRateAppDialogChoice Exception: $e")
        }
    }

    override suspend fun setPreviousRequestTimeInMillis(timeInMillis: Long) {
        try {
            settingsDataStore.edit {
                it[PreferencesKeys.PREVIOUS_REQUEST_TIME_IN_MILLIS] = timeInMillis
                Timber.i("PreviousRequestTimeInMillis edited to $timeInMillis")
            }
        } catch (e: Exception) {
            Timber.e("setPreviousRequestTimeInMillis Exception: $e")
        }
    }
}