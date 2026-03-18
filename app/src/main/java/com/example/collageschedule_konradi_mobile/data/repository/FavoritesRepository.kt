package com.example.collageschedule_konradi_mobile.data.repository

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(context: Context) {

    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val KEY = "favorite_groups"

    private val _favorites = MutableStateFlow(loadFavorites())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet(KEY, emptySet()) ?: emptySet()
    }

    fun addFavorite(group: String) {
        val updated = _favorites.value.toMutableSet().apply { add(group) }
        prefs.edit { putStringSet(KEY, updated) }
        _favorites.value = updated
    }

    fun removeFavorite(group: String) {
        val updated = _favorites.value.toMutableSet().apply { remove(group) }
        prefs.edit { putStringSet(KEY, updated) }
        _favorites.value = updated
    }

    fun isFavorite(group: String): Boolean = _favorites.value.contains(group)

    fun toggleFavorite(group: String) {
        if (isFavorite(group)) removeFavorite(group) else addFavorite(group)
    }
}
