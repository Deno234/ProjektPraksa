package com.example.aplikacijazaprognozuvremena.publicdata

object WeatherTranslation {
    private val WEATHER_TRANSLATIONS = mapOf(
        "Clear" to "Vedro",
        "Clouds" to "Oblaci",
        "Rain" to "Kiša",
        "Thunderstorm" to "Grmljavina",
        "Snow" to "Snijeg",
        "Fog" to "Magla",
        "Drizzle" to "Sitna kiša",
        "Mist" to "Magla",
        "Smoke" to "Dim"
    )

    fun translate(weatherCondition: String?): String? {
        return WEATHER_TRANSLATIONS.keys.find {
            it.equals(weatherCondition, ignoreCase = true)
        }?.let {
            WEATHER_TRANSLATIONS[it]
        } ?: weatherCondition
    }
}