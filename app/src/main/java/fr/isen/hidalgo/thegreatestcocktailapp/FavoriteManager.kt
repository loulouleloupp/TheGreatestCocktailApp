package fr.isen.hidalgo.thegreatestcocktailapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.Drink
import kotlin.collections.toMutableList

object FavoriteManager {
	private const val PREFS_NAME = "cocktail_prefs"
	private const val KEY_FAVORITES = "favorite_list_json"
	private val gson = Gson()

	fun toggleFavorite(context: Context, drink: Drink) {
		val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		val currentFavorites = getFavorites(context).toMutableList()

		// On compare par l'ID du cocktail
		val existing = currentFavorites.find { it.id == drink.id }

		if (existing != null) {
			currentFavorites.remove(existing)
		} else {
			currentFavorites.add(drink)
		}

		val json = gson.toJson(currentFavorites)
		prefs.edit().putString(KEY_FAVORITES, json).apply()
	}

	// Liste des boissons
	fun getFavorites(context: Context): List<Drink> {
		val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		val json = prefs.getString(KEY_FAVORITES, null) ?: return emptyList()

		val type = object : TypeToken<List<Drink>>() {}.type
		return try {
			gson.fromJson(json, type)
		} catch (e: Exception) {
			emptyList()
		}
	}

	// Pour savoir si le coeur doit être rouge au chargement
	fun isFavorite(context: Context, drinkId: String?): Boolean {
		if (drinkId == null) return false
		return getFavorites(context).any { it.id == drinkId }
	}
}