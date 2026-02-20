package fr.isen.hidalgo.thegreatestcocktailapp.network

import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CategoryResponse
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.DrinksByCategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

	@GET("random.php")
	fun getRandomCocktail(): Call<CocktailResponse>

	@GET("list.php?c=list")
	fun getCategories(): Call<CategoryResponse>

	@GET("filter.php")
	fun getDrinksByCategory(@Query("c") categoryName: String): Call<DrinksByCategoryResponse>

	@GET("lookup.php")
	fun getDrinkDetail(@Query("i") drinkId: String): Call<CocktailResponse>
}