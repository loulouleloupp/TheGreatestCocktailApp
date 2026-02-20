package fr.isen.hidalgo.thegreatestcocktailapp.dataClasses

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
	@SerializedName("drinks") val drinks: List<CategoryItem>?
)

data class CategoryItem(
	@SerializedName("strCategory") val name: String
)