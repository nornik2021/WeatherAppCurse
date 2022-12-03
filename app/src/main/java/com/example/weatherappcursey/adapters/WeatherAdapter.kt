package com.example.weatherappcursey.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherappcursey.R
import com.example.weatherappcursey.databinding.ListItemBinding

class WeatherAdapter : ListAdapter<WeatherModel, WeatherAdapter.Holder>(Comporator()) { // вьюхолдер хранит в себе всю разметку, в круглые скобки передаем компоратор
// листадаптер берет список и передает в рецайклервью
    class Holder(view: View): RecyclerView.ViewHolder(view) { // здесь сохраняется шаблон который мы создали
        val binding = ListItemBinding.bind(view)
       fun bind(item: WeatherModel) = with(binding){

           tvDate.text = item.time
           tvCondition.text = item.condition
           tvTemp.text = item.currentTemp

       }
    }

    class Comporator : DiffUtil.ItemCallback<WeatherModel>(){ // создаем компоратор и наследуемся от дифутил. Указываем какие элементы сравниваем

        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean { // функция сравнивает старый элемент старого списка с новым элементом из нового списка
            return oldItem == newItem // приравниваем старый элемент к новому. Если элементы уникальные - сравниваем по id
        }


        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean { // функция сравнивает старый элемент старого списка с новым элементом из нового списка
            return oldItem == newItem // приравниваем старый элемент к новому.
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder { // функция запускается столько раз сколько элементов в списке
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,false) // создаем разметку и наследуемся от парент
        return Holder(view) // возвращаем вью в холдер. Если список из 10 элементов то в памяти будет 10 вьюхлдеров
    }


    override fun onBindViewHolder(holder: Holder, position: Int) { // здесь холдер заполняется начиная с нулевой позиции
        holder.bind(getItem(position)) // получаем из нового списка новый элемент
    }
}