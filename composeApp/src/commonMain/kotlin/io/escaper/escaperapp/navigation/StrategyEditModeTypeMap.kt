package io.escaper.escaperapp.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlin.reflect.typeOf

typealias NavigationTypeMap = Map<KType, @JvmSuppressWildcards NavType<*>>

val StrategyEditModeTypeMap: NavigationTypeMap = mapOf(
    typeOf<StrategyEditMode>() to StrategyEditModeNavType()
)

private class StrategyEditModeNavType : NavType<StrategyEditMode>(isNullableAllowed = false) {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun get(bundle: SavedState, key: String): StrategyEditMode? {
        val value = bundle.read { getString(key) }
        return json.decodeFromString(value)
    }

    override fun parseValue(value: String): StrategyEditMode {
        return json.decodeFromString(value)
    }

    override fun put(bundle: SavedState, key: String, value: StrategyEditMode) {
        bundle.write { putString(key, json.encodeToString(value)) }
    }

    override fun serializeAsValue(value: StrategyEditMode): String {
        return json.encodeToString(value)
    }
}