package utils

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Последняя выбранная группа
    fun saveGroup(groupName: String) {
        prefs.edit().putString("selected_group", groupName).apply()
    }

    fun getSavedGroup(): String {
        return prefs.getString("selected_group", "ИС-12") ?: "ИС-12"
    }

    // Список избранного
    fun toggleFavorite(groupName: String) {
        val favorites = getFavorites().toMutableSet()
        if (favorites.contains(groupName)) favorites.remove(groupName)
        else favorites.add(groupName)
        prefs.edit().putStringSet("favorites_list", favorites).apply()
    }

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorites_list", emptySet()) ?: emptySet()
    }
}