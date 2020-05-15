package edu.umsl.duc_ngo.multipurpose.ui.weather

import android.content.Context
import android.preference.PreferenceManager

class WeatherPrefUtil {
    companion object {
        private const val USE_LOCATION = "edu.umsl.duc_ngo.use_location"
        private const val ZIP_CODE = "edu.umsl.duc_ngo.zip_code"
        private const val COUNTRY_CODE = "edu.umsl.duc_ngo.country_code"

        fun getUseLocation(context: Context): Boolean {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(USE_LOCATION, false)
        }

        fun setUseLocation(context: Context, boolean: Boolean) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putBoolean(USE_LOCATION, boolean)
            editor.apply()
        }

        fun getZipCode(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(ZIP_CODE, 63114)
        }

        fun setZipCode(context: Context, zipcode: Int) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(ZIP_CODE, zipcode)
            editor.apply()
        }

        fun getCountryCode(context: Context): String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(COUNTRY_CODE, "US")!!
        }

        fun setCountryCode(context: Context, country: String) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(COUNTRY_CODE, country)
            editor.apply()
        }
    }
}