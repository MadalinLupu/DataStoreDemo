package com.example.datastoredemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

// Ecran principal : montre DataStore (settings) + Image (fichier interne)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {

    // Observer le mode sombre (DataStore)
    val isDark by viewModel.darkMode.collectAsState(initial = false)

    // Observer le nom d'utilisateur (DataStore)
    val savedUsername by viewModel.username.collectAsState(initial = "")

    // Observer la photo de profil (fichier interne)
    val photo by viewModel.profilePhoto.collectAsState()

    // Charger la photo au demarrage
    LaunchedEffect(Unit) {
        viewModel.loadPhoto()
    }

    // Etat du champ de texte
    var nameInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo Stockage Local") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ============================================
            // SECTION 1 : Mode sombre (DataStore - Boolean)
            // ============================================
            Text(
                "1. Mode sombre (DataStore)",
                style = MaterialTheme.typography.titleMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode sombre")
                    Switch(
                        checked = isDark,
                        onCheckedChange = { viewModel.toggleDarkMode(isDark) }
                    )
                }
            }

            // Afficher l'etat actuel
            Text(
                text = if (isDark) "Sauvegarde dans DataStore : ON"
                else "Sauvegarde dans DataStore : OFF",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            // ============================================
            // SECTION 2 : Nom d'utilisateur (DataStore - String)
            // ============================================
            Text(
                "2. Nom d'utilisateur (DataStore)",
                style = MaterialTheme.typography.titleMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Votre nom") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (nameInput.isNotBlank()) {
                                viewModel.saveUsername(nameInput.trim())
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sauvegarder le nom")
                    }
                }
            }

            // Afficher le nom sauvegarde
            if (savedUsername.isNotEmpty()) {
                Text(
                    text = "Sauvegarde dans DataStore : $savedUsername",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            // ============================================
            // SECTION 3 : Photo de profil (fichier interne)
            // ============================================
            Text(
                "3. Photo de profil (fichier interne)",
                style = MaterialTheme.typography.titleMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Afficher la photo si elle existe
                    if (photo != null) {
                        Image(
                            bitmap = photo!!.asImageBitmap(),
                            contentDescription = "Photo de profil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Placeholder si pas de photo
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("Pas de photo")
                            }
                        }
                    }

                    // Bouton pour generer et sauvegarder une image de demo
                    Button(
                        onClick = {
                            val demoBitmap = createDemoBitmap()
                            viewModel.savePhoto(demoBitmap)
                        }
                    ) {
                        Text("Generer une photo demo")
                    }

                    // Bouton pour supprimer la photo
                    if (photo != null) {
                        OutlinedButton(
                            onClick = { viewModel.deletePhoto() }
                        ) {
                            Text("Supprimer la photo")
                        }
                    }
                }
            }

            // Info sur le stockage
            Text(
                text = "L'image est sauvegardee dans filesDir (stockage interne prive).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Creer un bitmap de demo (carre colore avec texte)
// Dans une vraie app, on prendrait une photo ou on choisirait dans la galerie
private fun createDemoBitmap(): Bitmap {
    val size = 300
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Fond colore aleatoire
    val colors = listOf(
        0xFF4CAF50.toInt(), 0xFF2196F3.toInt(),
        0xFFFF9800.toInt(), 0xFF9C27B0.toInt(),
        0xFFE91E63.toInt(), 0xFF00BCD4.toInt()
    )
    val bgPaint = Paint().apply { color = colors.random() }
    canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), bgPaint)

    // Texte au centre
    val textPaint = Paint().apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Demo", size / 2f, size / 2f + 15f, textPaint)

    return bitmap
}