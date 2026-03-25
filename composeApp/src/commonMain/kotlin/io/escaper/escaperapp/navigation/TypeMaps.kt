package io.escaper.escaperapp.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType
import kotlin.reflect.typeOf

typealias NavigationTypeMap = Map<KType, @JvmSuppressWildcards NavType<*>>

val StrategyEditModeTypeMap: NavigationTypeMap = mapOf(
    typeOf<EscaperScreen.EditStrategyScreen.StrategyEditMode>() to StrategyEditModeNavType()
)

private class StrategyEditModeNavType : NavType<EscaperScreen.EditStrategyScreen.StrategyEditMode>(
    isNullableAllowed = false,
) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: EscaperScreen.EditStrategyScreen.StrategyEditMode
    ) {
        when (value) {
            EscaperScreen.EditStrategyScreen.StrategyEditMode.Create -> bundle.write {
                putString(key, CREATE_NAME)
            }

            is EscaperScreen.EditStrategyScreen.StrategyEditMode.Update -> bundle.write {
                putString(key, "$UPDATE_NAME$SEPARATOR${value.strategyId}")
            }
        }
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): EscaperScreen.EditStrategyScreen.StrategyEditMode {
        val string = bundle.read { getString(key) }
        return parseValue(string)
    }

    override fun parseValue(value: String): EscaperScreen.EditStrategyScreen.StrategyEditMode {
        val parts = value.split(SEPARATOR)
        return when (parts.firstOrNull()) {
            CREATE_NAME -> EscaperScreen.EditStrategyScreen.StrategyEditMode.Create
            UPDATE_NAME if parts.size == 2 -> {
                EscaperScreen.EditStrategyScreen.StrategyEditMode.Update(
                    parts.lastOrNull().orEmpty()
                )
            }

            else -> error("Invalid value in bundle parse value")
        }
    }

    companion object {
        private const val CREATE_NAME = "create"
        private const val UPDATE_NAME = "update"
        private const val SEPARATOR = '|'
    }

}