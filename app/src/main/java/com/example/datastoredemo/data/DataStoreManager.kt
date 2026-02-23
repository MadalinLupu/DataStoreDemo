package com.example.datastoredemo.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension : ajoute la propriete "dataStore" au Context
// "settings" = nom du fichier de preferences sur le disque
val Context.dataStore by preferencesDataStore(name = "settings")

// Classe qui gere la lecture et l'ecriture des settings
class DataStoreManager(private val context: Context) {

    // --- Cles de preferences ---
    // Chaque cle a un nom unique et un type (Boolean, String, Int, etc.)

    private val DARK_MODE = booleanPreferencesKey("dark_mode")
    private val USERNAME = stringPreferencesKey("username")

    // =============================================
    // Mode sombre (Boolean)
    // =============================================

    // Lecture : retourne un Flow<Boolean> qui se met a jour automatiquement
    // Si rien n'a ete sauvegarde, la valeur par defaut est false
    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[DARK_MODE] ?: false
        }

    // Ecriture : sauvegarde la valeur du mode sombre
    // suspend = fonction asynchrone (ne bloque pas l'ecran)
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }

    // =============================================
    // Nom d'utilisateur (String)
    // =============================================

    // Lecture : retourne un Flow<String>
    val usernameFlow: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[USERNAME] ?: ""
        }

    // Ecriture : sauvegarde le nom
    suspend fun setUsername(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = name
        }
    }
}