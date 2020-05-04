package edu.umsl.duc_ngo.multipurpose.ui.weather

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import edu.umsl.duc_ngo.multipurpose.R
import edu.umsl.duc_ngo.multipurpose.ui.BaseFragment
import kotlinx.android.synthetic.main.weather_fragment.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class WeatherFragment : BaseFragment() {
    companion object {
        fun newInstance() = WeatherFragment()

        private lateinit var intent: Intent

        @JvmStatic
        fun newIntentInit(context: FragmentActivity?): Intent {
            val intent = Intent(context, WeatherActivity::class.java)
            Companion.intent = intent
            return intent
        }
    }

    /* Global Attributes */
    private var units = "imperial"
    private val apiKey = "8118ed6ee68db2debfaaa5a44c832918"
    private var areaCode = "63114,us"
    private var requestMessage = ""
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
            ViewModelProvider(it).get(WeatherViewModel::class.java)
        }!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Monitor the current list data */
        viewModel.getIsFetch().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                /* Gson Doc: https://github.com/google/gson */
                val gson = GsonBuilder().create()
                val openWeatherApiData = gson.fromJson(viewModel.getJsonResult(), OpenWeatherApiData::class.java)
                _loader.visibility = View.GONE

                /* OpenWeatherApi Doc: https://openweathermap.org/current */
                /* Location */
                val address = "${openWeatherApiData.name}, ${openWeatherApiData.sys.country}"
                _address_text.text = address

                /* Last Updated: Get current local date time */
                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                val formatted = currentTime.format(formatter)
                _updated_at_text.text = formatted

                /* Max and Min temperature */
                val minTemp = "Min Temp: ${kotlin.math.ceil(openWeatherApiData.main.temp_min).toInt()}°F"
                val maxTemp = "Max Temp: ${kotlin.math.ceil(openWeatherApiData.main.temp_max).toInt()}°F"
                _temp_min_text.text = minTemp
                _temp_max_text.text = maxTemp

                /* Temperature (the color of background will alter, depend on the temperature */
                val temperature = "${kotlin.math.ceil(openWeatherApiData.main.temp).toInt()}°F"
                val feelsLike = "Feels Like: ${kotlin.math.ceil(openWeatherApiData.main.feels_like).toInt()}°F"
                val colorLabel = when {
                    openWeatherApiData.main.temp < 30 -> "#00BCD4"
                    openWeatherApiData.main.temp < 70 -> "#1BCA98"
                    else -> "#FF695E"
                }
                val drawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.custom_background)!!
                DrawableCompat.setTint(drawable, Color.parseColor(colorLabel))
                _status_text.text = openWeatherApiData.weather[0].main
                _temp_text.text = temperature
                _feels_like_text.text = feelsLike
                _weather_background.background = drawable

                /* Details */
                val sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(openWeatherApiData.sys.sunrise * 1000))
                val sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(openWeatherApiData.sys.sunset * 1000))
                val wind = "${openWeatherApiData.wind.speed} mph"
                val pressure = "${openWeatherApiData.main.pressure} hPa"
                val humidity = "${openWeatherApiData.main.humidity} %"
                _sunrise_text.text = sunrise
                _sunset_text.text = sunset
                _wind_text.text = wind
                _pressure_text.text = pressure
                _humidity_text.text = humidity
            } else {
                _loader.visibility = View.VISIBLE
                fetchWeatherData()
            }
        })
    }

    private fun fetchWeatherData() {
        /* OkHttp Doc: https://square.github.io/okhttp/ */
        val url = "https://api.openweathermap.org/data/2.5/weather?zip=$areaCode&units=$units&appid=$apiKey"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                requestMessage = "An error occurred! Failed to fetch data..."
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    viewModel.setJsonResult(response.body?.string()!!)
                    viewModel.setIsFetch(true)
                }
            }
        })
    }

    /* OpenWeatherApi object data (for Gson) */
    data class OpenWeatherApiData(
        val weather: List<WeatherData>,
        val main: MainData,
        val wind: WindData,
        val sys: SysData,
        val name: String
    )

    data class WeatherData(val main: String)
    data class MainData(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Double,
        val humidity: Double
    )

    data class WindData(val speed: Double)
    data class SysData(val country: String, val sunrise: Long, val sunset: Long)
}