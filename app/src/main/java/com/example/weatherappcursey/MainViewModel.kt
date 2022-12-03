package com.example.weatherappcursey

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappcursey.adapters.WeatherModel

class MainViewModel: ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherModel>()// передаем список из дата класса
    val liveDataList = MutableLiveData<List<WeatherModel>>()// передаем список из дата класса

}