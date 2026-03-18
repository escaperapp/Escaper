package io.escaper.escaperapp.domain

enum class AppLanguage(
    val isoCode: String,
) {
    English(
        isoCode = "en"
    ),
    Russian(
        isoCode = "ru"
    );

    companion object {
        fun fromPlatformCode(localeCode: String): AppLanguage {
            val isoCode = localeCode
                .substringBefore('-')
                .substringBefore('_')
                .lowercase()
            return AppLanguage.entries.find { it.isoCode == isoCode } ?: English
        }

        fun fromIsoCode(isoCode: String): AppLanguage? {
            return AppLanguage.entries.find { it.isoCode == isoCode }
        }
    }
}