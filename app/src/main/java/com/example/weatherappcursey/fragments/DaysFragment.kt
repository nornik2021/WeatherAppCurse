package com.example.weatherappcursey.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherappcursey.R


 class DaysFragment : Fragment() {

    override fun onCreateView( // создаем метод создание вью
        inflater: LayoutInflater, container: ViewGroup?, // надуывем он криейт с помощью метода inflater
        savedInstanceState: Bundle?, // предоставляет возможность сохранить пользовательский интерфейс активности в объект Bundle
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}
// создали фрагмент для Days