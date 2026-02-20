package fr.isen.hidalgo.thegreatestcocktailapp.network

import fr.isen.hidalgo.thegreatestcocktailapp.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
	private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

	val apiService: ApiService by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ApiService::class.java)
	}
}