package com.example.weatherappcursey.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpAdapter(fa: FragmentActivity, private val list: List<Fragment>) : FragmentStateAdapter (fa) { // унаследуется от фрагментстейтадаптера, передаем в него объект фрагмент активити (fa)

    override fun getItemCount(): Int {
        return list.size // передаем кол-во элементов и при переключении передает в createFragment нужную позицию. Данные берет из
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}
// создали вьюпейджэер адаптер для переключения между фрагментами