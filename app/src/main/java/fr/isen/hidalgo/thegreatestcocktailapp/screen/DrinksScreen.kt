package fr.isen.hidalgo.thegreatestcocktailapp.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.hidalgo.thegreatestcocktailapp.DetailCocktailActivity
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.DrinkSummary
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.DrinksByCategoryResponse
import fr.isen.hidalgo.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksScreen(modifier: Modifier = Modifier, categoryName: String?) {
    var drinks by remember { mutableStateOf<List<DrinkSummary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categoryName) {
        if (categoryName != null) {
            val call = NetworkManager.apiService.getDrinksByCategory(categoryName)
            call.enqueue(object : Callback<DrinksByCategoryResponse> {
                override fun onResponse(
                    call: Call<DrinksByCategoryResponse>,
                    response: Response<DrinksByCategoryResponse>
                ) {
                    isLoading = false
                    if (response.isSuccessful) {
                        response.body()?.drinks?.let {
                            drinks = it
                        }
                    }
                }

                override fun onFailure(call: Call<DrinksByCategoryResponse>, t: Throwable) {
                    isLoading = false
                    Log.e("DrinksScreen", "Network error : ${t.message}")
                }
            })
        }
    }

    val filteredDrinks = drinks.filter {
        it.name?.contains(searchQuery, ignoreCase = true) == true
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search in $categoryName...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFD35400)) },
                shape = RoundedCornerShape(25.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    focusedBorderColor = Color(0xFFD35400),
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFFD35400),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }

                items(filteredDrinks) { drink ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(context, DetailCocktailActivity::class.java)
                                intent.putExtra("COCKTAIL_ID", drink.id)
                                context.startActivity(intent)
                            },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = drink.thumbUrl,
                                contentDescription = drink.name,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = drink.name ?: "",
                                modifier = Modifier.padding(12.dp),
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}