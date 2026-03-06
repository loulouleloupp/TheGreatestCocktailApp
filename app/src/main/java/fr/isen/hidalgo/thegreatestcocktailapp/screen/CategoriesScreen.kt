package fr.isen.hidalgo.thegreatestcocktailapp.screen

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.hidalgo.thegreatestcocktailapp.DrinksActivity
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CategoryItem
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CategoryResponse
import fr.isen.hidalgo.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material3.OutlinedTextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var categories by remember { mutableStateOf<List<CategoryItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    // Récupération des catégories avec l'API
    LaunchedEffect(Unit) {
        NetworkManager.apiService.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    categories = response.body()?.drinks ?: emptyList()
                }
            }
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                isLoading = false
            }
        })
    }

    // Filtrage
    val filteredCategories = categories.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search for a category...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFD35400)) },
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.9f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                disabledContainerColor = Color.White.copy(alpha = 0.9f),
                focusedBorderColor = Color(0xFFD35400),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFFD35400)
            )
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredCategories) { category ->
                    CategoryCard(category.name) {
                        val intent = Intent(context, DrinksActivity::class.java)
                        intent.putExtra("CATEGORY_NAME", category.name)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.uppercase(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                color = Color(0xFFD35400),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}