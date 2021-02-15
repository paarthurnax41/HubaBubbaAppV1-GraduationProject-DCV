package com.example.hubabubbaapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MAXPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MINPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_PARTICIPANT
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_TYPE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedType = stringPreferencesKey("Type")
        val selectedTypeId = intPreferencesKey("TypeId")
        val selectedMinPrice = doublePreferencesKey("minPrice")
        val selectedMinPriceId = intPreferencesKey("minPriceId")
        val selectedParticipant = intPreferencesKey("Participant")
        val selectedParticipantId = intPreferencesKey("ParticipantId")
        val selectedMaxPrice = doublePreferencesKey("maxPrice")
        val selectedMaxPriceId = intPreferencesKey("maxPriceId")


    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "Bored_Preferences"
    )

    suspend fun saveTypePriceParticipant(
        type: String,
        typeId: Int,
        minPrice: Double,
        minPriceId: Int,
        participant: Int,
        participantId: Int,
        maxPrice: Double,
        maxPriceId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedType] = type
            preferences[PreferenceKeys.selectedTypeId] = typeId
            preferences[PreferenceKeys.selectedMinPrice] = minPrice
            preferences[PreferenceKeys.selectedMinPriceId] = minPriceId
            preferences[PreferenceKeys.selectedParticipant] = participant
            preferences[PreferenceKeys.selectedParticipantId] = participantId
            preferences[PreferenceKeys.selectedMaxPrice] = maxPrice
            preferences[PreferenceKeys.selectedMaxPriceId] = maxPriceId
        }
    }

    val readTypePriceParticipant: Flow<TypeAndPriceAndParticipants> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else run {
                throw exception
            }

        }
        .map { preferences ->
            val selectedType = preferences[PreferenceKeys.selectedType] ?: DEFAULT_TYPE
            val selectedTypeId = preferences[PreferenceKeys.selectedTypeId] ?: 0
            val selectedMinPrice = preferences[PreferenceKeys.selectedMinPrice] ?: DEFAULT_MINPRICE
            val selectedMinPriceId = preferences[PreferenceKeys.selectedMinPriceId] ?: 0
            val selectedParticipant =
                preferences[PreferenceKeys.selectedParticipant] ?: DEFAULT_PARTICIPANT
            val selectedParticipantId = preferences[PreferenceKeys.selectedParticipantId] ?: 0
            val selectedMaxPrice = preferences[PreferenceKeys.selectedMaxPrice] ?: DEFAULT_MAXPRICE
            val selectedMaxPriceId = preferences[PreferenceKeys.selectedMaxPriceId] ?: 0


            TypeAndPriceAndParticipants(
                selectedType,
                selectedTypeId,
                selectedMinPrice,
                selectedMinPriceId,
                selectedParticipant,
                selectedParticipantId,
                selectedMaxPrice,
                selectedMaxPriceId
            )
        }
}

data class TypeAndPriceAndParticipants(
    val selectedType: String,
    val selectedTypeId: Int,
    val selectedMinPrice: Double,
    val selectedMinPriceId: Int,
    val selectedParticipant: Int,
    val selectedParticipantId: Int,
    val selectedMaxPrice: Double,
    val selectedMaxPriceId: Int,
)