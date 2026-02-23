package com.example.datastoredemo.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

// Fonctions utilitaires pour sauvegarder et lire des images
// On utilise les fichiers internes de l'app (context.filesDir)
object ImageStorage {

    // Sauvegarder un bitmap en fichier PNG dans le stockage interne
    fun saveImage(context: Context, bitmap: Bitmap, filename: String): String {
        // Creer un fichier dans le dossier prive de l'app
        val file = File(context.filesDir, filename)

        // Ouvrir un flux d'ecriture et compresser l'image en PNG
        // use {} ferme automatiquement le flux (evite les fuites memoire)
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }

        return filename
    }

    // Lire une image depuis le stockage interne
    // Retourne null si le fichier n'existe pas
    fun loadImage(context: Context, filename: String): Bitmap? {
        val file = File(context.filesDir, filename)

        return if (file.exists()) {
            // Decoder le fichier en Bitmap
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    // Supprimer une image du stockage interne
    fun deleteImage(context: Context, filename: String): Boolean {
        val file = File(context.filesDir, filename)
        return file.delete()
    }
}