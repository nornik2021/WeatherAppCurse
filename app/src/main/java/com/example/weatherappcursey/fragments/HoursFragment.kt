package com.example.weatherappcursey.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappcursey.R
import com.example.weatherappcursey.adapters.WeatherAdapter
import com.example.weatherappcursey.adapters.WeatherModel
import com.example.weatherappcursey.databinding.FragmentHoursBinding
import com.example.weatherappcursey.databinding.FragmentMainBinding


class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding

    private lateinit var adapter: WeatherAdapter// адаптер создаем на уровне класса

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       binding = FragmentHoursBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView() // запускаем рецайклер вью
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcView.adapter = adapter
        val list = listOf(
            WeatherModel("Tivat","12:00", "Sunny","25°C", "31°C","","",""),
            WeatherModel("Tivat","13:00", "Sunny","27°C", "31°C","","",""),
            WeatherModel("Tivat","14:00", "Sunny","32°C", "31°C","","",""),
        )
        adapter.submitList(list) // загружаев в адаптер список

    }

    companion object {

        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}