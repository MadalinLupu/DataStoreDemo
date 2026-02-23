package com.example.datastoredemo
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.datastoredemo.data.DataStoreManager
import com.example.datastoredemo.data.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel : fait le lien entre les donnees et l'ecran
class MainViewModel(
    private val dataStoreManager: DataStoreManager,
    private val context: Context
) : ViewModel() {

    // =============================================
    // Settings (DataStore)
    // =============================================

    // Observer le mode sombre
    val darkMode = dataStoreManager.darkModeFlow

    // Observer le nom d'utilisateur
    val username = dataStoreManager.usernameFlow

    // Basculer le mode sombre ON/OFF
    fun toggleDarkMode(currentValue: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkMode(!currentValue)
        }
    }

    // Sauvegarder le nom d'utilisateur
    fun saveUsername(name: String) {
        viewModelScope.launch {
            dataStoreManager.setUsername(name)
        }
    }

    // =============================================
    // Image (fichiers internes)
    // =============================================

    companion object {
        // Nom du fichier image sur le disque
        private const val PHOTO_FILENAME = "photo_profil.png"
    }

    // Image de profil chargee en memoire
    private val _profilePhoto = MutableStateFlow<Bitmap?>(null)
    val profilePhoto: StateFlow<Bitmap?> = _profilePhoto

    // Charger l'image de profil depuis les fichiers internes
    fun loadPhoto() {
        _profilePhoto.value = ImageStorage.loadImage(context, PHOTO_FILENAME)
    }

    // Sauvegarder une nouvelle photo de profil
    fun savePhoto(bitmap: Bitmap) {
        viewModelScope.launch {
            ImageStorage.saveImage(context, bitmap, PHOTO_FILENAME)
            _profilePhoto.value = bitmap
        }
    }

    // Supprimer la photo de profil
    fun deletePhoto() {
        ImageStorage.deleteImage(context, PHOTO_FILENAME)
        _profilePhoto.value = null
    }
}

// Factory : necessaire pour passer les dependances au ViewModel
class MainViewModelFactory(
    private val dataStoreManager: DataStoreManager,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStoreManager, context) as T
        }
        throw IllegalArgumentException("ViewModel inconnu")
    }
}