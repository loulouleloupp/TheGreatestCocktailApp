package fr.isen.hidalgo.thegreatestcocktailapp.dataClasses

import com.google.gson.annotations.SerializedName

data class DrinksByCategoryResponse(
	@SerializedName("drinks") val drinks: List<DrinkSummary>?
)

data class DrinkSummary(
	@SerializedName("strDrink") val name: String,
	@SerializedName("strDrinkThumb") val thumbUrl: String,
	@SerializedName("idDrink") val id: String
)