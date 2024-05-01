package com.example.mapsappogg.models

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersSettings(private val context: Context) {

    // CREAR DATABASE
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")
        val STORE_USERNAME = stringPreferencesKey("store_username")
        val STORE_USERPASS = stringPreferencesKey("store_userpass")
    }

    val getUserData: Flow<List<String>> = context.dataStore.data.map { prefs ->
        listOf(
            prefs[STORE_USERNAME] ?: "",
            prefs[STORE_USERPASS] ?: ""
        )
    }

    // BORRAR/ELIMINAR DADES DE L'USUARI
    suspend fun deleteUserData() {
        this.context.dataStore.edit { prefs ->
            prefs[STORE_USERNAME] = ""
            prefs[STORE_USERPASS] = ""
        }
    }

    // GUARDAR/EMMAGATZEMAR DADES DE L'USUARI
    suspend fun saveUserData(userName: String, userPass: String) {
        this.context.dataStore.edit { prefs ->
            prefs[STORE_USERNAME] = userName
            prefs[STORE_USERPASS] = userPass
        }
    }
}