package com.jmm.brsap.meditell.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

    val welcomeStatus: Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            val pref = preferences[WELCOME_STATUS] ?: NEW_USER
            pref
        }



    val userId: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            val pref = preferences[USER_ID] ?: ""
            pref
        }

    val userName: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            val pref = preferences[USER_NAME] ?: ""
            pref
        }

    val firstName: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            val pref = preferences[USER_FIRST_NAME] ?: ""
            pref
        }

    val lastName: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            val pref = preferences[USER_LAST_NAME] ?: ""
            pref
        }

    suspend fun updateWelcomeStatus(status: Int) {
        context.dataStore.edit { preference ->
            preference[WELCOME_STATUS] = status
        }
    }

    suspend fun updateUserId(userId: String) {
        context.dataStore.edit { preference ->
            preference[USER_ID] = userId
        }
    }

    suspend fun updateUserName(userName: String) {
        context.dataStore.edit { preference ->
            preference[USER_NAME] = userName
        }
    }

    suspend fun updateUserFirstName(userName: String) {
        context.dataStore.edit { preference ->
            preference[USER_FIRST_NAME] = userName
        }
    }

    suspend fun updateUserLastName(userName: String) {
        context.dataStore.edit { preference ->
            preference[USER_LAST_NAME] = userName
        }
    }


    suspend fun clearUserInfo() {
        updateUserId("")
        updateUserName("")
        updateUserFirstName("")
        updateUserLastName("")
        updateWelcomeStatus(0)
        delay(1000)
    }




    companion object {
        const val USER_PREFERENCES_NAME = "PocketMoney"

        const val NEW_USER = 0
        const val LOGIN_DONE = 1



        val WELCOME_STATUS = intPreferencesKey("welcome_status")


        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
        val USER_LAST_NAME = stringPreferencesKey("user_last_name")
    }
}


