package fr.isen.hidalgo.thegreatestcocktailapp.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.DrinkSummary
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.DrinksByCategoryResponse
import fr.isen.hidalgo.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DrinksScreen(modifier: Modifier = Modifier, categoryName: String?) {
    var drinks by remember { mutableStateOf<List<DrinkSummary>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current


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
//                        drinks = response.body()?.drinks ?: emptyList()
                        response.body()?.drinks?.let {
                            drinks = it
                            //permet d'afficher les categories si ce n'est pas nul
                        }
                    }
                }

                override fun onFailure(call: Call<DrinksByCategoryResponse>, t: Throwable) {
                    isLoading = false
                    Log.e("DrinksScreen", "Erreur réseau : ${t.message}")
                }
            })
        }
    }

    if (isLoading) {

        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            items(drinks) { drink ->
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
