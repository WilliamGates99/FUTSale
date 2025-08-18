package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.data.repositories

import androidx.datastore.core.DataStore
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.di.MiscellaneousDataStoreQualifier
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.MiscellaneousPreferences
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.models.RateAppOption
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.AppUpdateDialogShowCount
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.IsAppUpdateDialogShownToday
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.MiscellaneousDataStoreRepository
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.repositories.PreviousRateAppRequestTimeInMs
import com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.domain.utils.DateHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MiscellaneousDataStoreRepositoryImpl @Inject constructor(
    @MiscellaneousDataStoreQualifier private val dataStore: DataStore<MiscellaneousPreferences>
) : MiscellaneousDataStoreRepository {

    override fun getAppUpdateDialogShowCount(): Flow<AppUpdateDialogShowCount> =
        dataStore.data.map {
            it.appUpdateDialogShowCount
        }.catch { e ->
            Timber.e("Get app update dialog show count failed:")
            e.printStackTrace()
        }

    override fun isAppUpdateDialogShownToday(): Flow<IsAppUpdateDialogShownToday> =
        dataStore.data.map {
            val dialogShowEpochDays = it.appUpdateDialogShowEpochDays

            dialogShowEpochDays?.let { epochDays ->
                val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val dialogShowLocalDate = LocalDate.fromEpochDays(epochDays)

                val isShownToday = dialogShowLocalDate.periodUntil(todayDate).days == 0

                isShownToday
            } ?: false
        }.catch { e ->
            Timber.e("Get is app update dialog shown today failed:")
            e.printStackTrace()
        }

    override fun getSelectedRateAppOption(): Flow<RateAppOption> = dataStore.data.map {
        it.selectedRateAppOption
    }.catch { e ->
        Timber.e("Get selected rate app option failed:")
        e.printStackTrace()
    }

    override fun getPreviousRateAppRequestTimeInMs(): Flow<PreviousRateAppRequestTimeInMs?> =
        dataStore.data.map {
            it.previousRateAppRequestTimeInMs
        }.catch { e ->
            Timber.e("Get previous rate app request time in ms failed:")
            e.printStackTrace()
        }

    override suspend fun storeAppUpdateDialogShowCount(count: Int) {
        try {
            dataStore.updateData { it.copy(appUpdateDialogShowCount = count) }
            Timber.i("App update dialog show count edited to $count")
        } catch (e: Exception) {
            Timber.e("Store app update dialog show count failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeAppUpdateDialogShowEpochDays() {
        try {
            val todayLocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val todayEpochDays = todayLocalDate.toEpochDays()
            dataStore.updateData { it.copy(appUpdateDialogShowEpochDays = todayEpochDays.toInt()) }
            Timber.i("App update dialog show epoch days edited to $todayEpochDays")
        } catch (e: Exception) {
            Timber.e("Store app update dialog show epoch days failed:")
            e.printStackTrace()
        }
    }

    override suspend fun removeAppUpdateDialogShowEpochDays() {
        try {
            dataStore.updateData { it.copy(appUpdateDialogShowEpochDays = null) }
            Timber.i("App update dialog show epoch days removed")
        } catch (e: Exception) {
            Timber.e("Remove App update dialog show epoch days failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storeSelectedRateAppOption(rateAppOption: RateAppOption) {
        try {
            dataStore.updateData { it.copy(selectedRateAppOption = rateAppOption) }
            Timber.i("Selected rate app option edited to ${rateAppOption.value}")
        } catch (e: Exception) {
            Timber.e("Store selected rate app option failed:")
            e.printStackTrace()
        }
    }

    override suspend fun storePreviousRateAppRequestTimeInMs() {
        try {
            val currentTimeInMs = DateHelper.getCurrentTimeInMillis()
            dataStore.updateData { it.copy(previousRateAppRequestTimeInMs = currentTimeInMs) }
            Timber.i("Previous rate app request time in ms edited to $currentTimeInMs")
        } catch (e: Exception) {
            Timber.e("Store previous rate app request time in ms failed:")
            e.printStackTrace()
        }
    }
}