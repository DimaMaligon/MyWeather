package com.example.myweather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myweather.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonSend.setOnClickListener {
                val city: String? = editUserField.text.toString()
                if (city == "") Toast.makeText(
                    this@MainActivity,
                    "Введите город",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    sendGetApiWeather(city)
                }
            }
        }
    }

    private fun sendGetApiWeather(city: String?) {
        val queue = Volley.newRequestQueue(applicationContext)
        val request = StringRequest(
            com.android.volley.Request.Method.GET,
            "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=0b75c56ff56f95f6a38503dedac82569",
            { response ->
                val data = parseResponseWeather(response)
                val weatherCelsium = (data.currentTemp.toDouble() - 273.15).toInt().let {
                    if(it > 0){
                        "+ $it"
                    }
                    else it
                }
                binding.textResult.apply {
                    visibility = View.VISIBLE
                    text = "${data.city} - погода: $weatherCelsium"
                }
            },
            { error ->
                Log.d("ErrorRequest", "bad request $error")
            }
        )
        queue.add(request)
    }

    private fun parseResponseWeather(result: String): Weather {
        val mainObject = JSONObject(result)
        val item = Weather(
            mainObject.getString("name"),
            mainObject.getString("timezone"),
            mainObject.getJSONObject("main").getString("temp")
        )
        return item
    }
}