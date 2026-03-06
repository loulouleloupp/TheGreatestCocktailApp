package fr.isen.hidalgo.thegreatestcocktailapp

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.hidalgo.thegreatestcocktailapp.network.NetworkManager
import fr.isen.hidalgo.thegreatestcocktailapp.screen.BottomAppBar
import fr.isen.hidalgo.thegreatestcocktailapp.screen.CategoriesScreen
import fr.isen.hidalgo.thegreatestcocktailapp.screen.DetailCocktailScreen
import fr.isen.hidalgo.thegreatestcocktailapp.screen.FavoriteScreen
import fr.isen.hidalgo.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                var isFavorite by remember { mutableStateOf(false) }
                var currentDrink by remember { mutableStateOf<Drink?>(null) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val tabItems = listOf(
                    TabBarItem("Random", Icons.Filled.Search, Icons.Filled.Search),
                    TabBarItem("Categories", Icons.Filled.Menu, Icons.Filled.Menu),
                    TabBarItem("Favorites", Icons.Filled.Favorite, Icons.Filled.FavoriteBorder)
                )

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
                                    Text("The Greatest Cocktail", color = Color.White, fontWeight = FontWeight.Bold)
                                },
                                actions = {
                                    // Condition d'affichage du bouton like
                                    if (currentRoute == "Random" && currentDrink != null) {
                                        IconButton(onClick = {
                                            FavoriteManager.toggleFavorite(context, currentDrink!!)
                                            isFavorite = FavoriteManager.isFavorite(context, currentDrink?.id)
                                        }) {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                                contentDescription = "Favoris",
                                                tint = if (isFavorite) Color.Red else Color.White
                                            )
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                            )
                        },
                        bottomBar = { BottomAppBar(tabItems, navController) }
                    ) { innerPadding ->
                        NavHost(navController, startDestination = "Random") {

                            composable(route = "Random") {
                                LaunchedEffect(Unit) {
                                    val call = NetworkManager.apiService.getRandomCocktail()
                                    call.enqueue(object : retrofit2.Callback<CocktailResponse> {
                                        override fun onResponse(
                                            call: retrofit2.Call<CocktailResponse>,
                                            response: retrofit2.Response<CocktailResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                val drink = response.body()?.drinks?.firstOrNull()
                                                currentDrink = drink
                                                isFavorite = FavoriteManager.isFavorite(context, drink?.id)
                                            }
                                        }
                                        override fun onFailure(call: retrofit2.Call<CocktailResponse>, t: Throwable) {
                                            Log.e("network", "Error : ${t.message}")
                                        }
                                    })
                                }

                                if (currentDrink != null) {
                                    DetailCocktailScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        drink = currentDrink
                                    )
                                } else {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = Color.White)
                                    }
                                }
                            }

                            composable(route = "Categories") {
                                CategoriesScreen(Modifier.padding(innerPadding))
                            }

                            composable("Favorites") {
                                FavoriteScreen(
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}