package com.example.weatherappcursey.adapters

data class WeatherModel( // создается для передачи всех переменных в адаптер и после показывает всю информацию
    val city: String,
    val time: String,
    val condition: String,
    val currentTemp: String,
    val maxTemp: String,
    val minTemp: String,
    val ImageUrl: String,
    val hours: String,

)
