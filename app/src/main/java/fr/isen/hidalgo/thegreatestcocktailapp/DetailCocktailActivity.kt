package fr.isen.hidalgo.thegreatestcocktailapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.hidalgo.thegreatestcocktailapp.network.NetworkManager
import fr.isen.hidalgo.thegreatestcocktailapp.screen.DetailCocktailScreen
import fr.isen.hidalgo.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material.icons.automirrored.filled.ArrowBack

class DetailCocktailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // je recupere l'ID depuis l'Intent
        val idDrink = intent.getStringExtra("COCKTAIL_ID")

        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                // stock l'objet Drink dans l'Activity
                var currentDrink by remember { mutableStateOf<Drink?>(null) }
                var isFavorite by remember { mutableStateOf(false) }
                val context = LocalContext.current

                //  charge la donnée avant de l'envoyer au Screen
                LaunchedEffect(idDrink) {
                    val call = if (idDrink != null) {
                        NetworkManager.apiService.getDrinkDetail(idDrink)
                    } else {
                        NetworkManager.apiService.getRandomCocktail()
                    }

                    call.enqueue(object : Callback<CocktailResponse> {
                        override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                            if (response.isSuccessful) {
                                // extrait le premier cocktail de la liste
                                currentDrink = response.body()?.drinks?.firstOrNull()
                            }
                        }
                        override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                            Log.e("DetailActivity", "Erreur : ${t.message}")
                        }
                    })
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.orange_500),
                                    colorResource(id = R.color.orange_700)
                                )
                            )
                        )
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = currentDrink?.name ?: "Détails",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        finish()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Retour",
                                            tint = Color.White
                                        )
                                    }
                                },
                                actions = {
                                    if (currentDrink != null) {
                                        IconButton(onClick = {
                                            isFavorite = !isFavorite
                                            val message = if (isFavorite) "Ajouté !" else "Retiré"
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        }) {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                                contentDescription = "Favoris",
                                                tint = if (isFavorite) Color.Red else Color.White
                                            )
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.Transparent,
                                    titleContentColor = Color.White,
                                    actionIconContentColor = Color.White
                                )
                            )
                        }
                    ) { innerPadding ->
                        DetailCocktailScreen(
                            modifier = Modifier.padding(innerPadding),
                            drink = currentDrink
                        )
                    }
                }
            }
        }
    }

    override fun onPause() { super.onPause(); Log.d("tags", "onPause") }
    override fun onRestart() { super.onRestart(); Log.d("tags", "onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d("tags", "onDestroy") }
    override fun onResume() { super.onResume(); Log.d("tags", "onResume") }
    override fun onStop() { super.onStop(); Log.d("tags", "onStop") }
}