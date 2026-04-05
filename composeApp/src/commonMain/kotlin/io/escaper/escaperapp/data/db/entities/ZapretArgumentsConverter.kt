package io.escaper.escaperapp.data.db.entities

import androidx.room.TypeConverter
import io.escaper.escaperapp.domain.args.ZapretArgument
import io.escaper.escaperapp.platform.PlatformProvider

class ZapretArgumentsConverter {

    @TypeConverter
    fun fromList(args: List<ZapretArgument<*, *>>?): String {
        if (args.isNullOrEmpty()) return ""

        return args.joinToString(ARG_SEPARATOR) { it.asStringArg() }
    }

    @TypeConverter
    fun toList(raw: String?): List<ZapretArgument<*, *>> {
        if (raw.isNullOrBlank()) return emptyList()

        val executableType = PlatformProvider.platform.executableType

        return raw.split(ARG_SEPARATOR)
            .mapNotNull { argString ->
                ZapretArgument.fromStringArg(argString, executableType)
            }
    }

    private companion object {
        const val ARG_SEPARATOR = ";;;"
    }
}