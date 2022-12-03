package com.example.weatherappcursey.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherappcursey.MainViewModel
import com.example.weatherappcursey.R
import com.example.weatherappcursey.adapters.VpAdapter
import com.example.weatherappcursey.adapters.WeatherModel
import com.example.weatherappcursey.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY ="289e6016143546d2aa7140324222511"

class MainFragment : Fragment() { // класс наследуется от Fragment
    private val flist = listOf( // список по которому мы переключаемся
        HoursFragment.newInstance(), //
        DaysFragment.newInstance()  //
    )
    private val tList = listOf( // список для TabLayoutMediator
        "Hours",
        "Days"
    )
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels() // иницилизировали класс MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()  // запускаем функцию инит (которая запускает адаптер)
        requestWeatherData("Belgrade")// запускаем функцию с поиском города
        updateCurrentCard()
    }

    // подключаем адаптер к вью пейджеру
    private fun init() = with(binding) { // адаптер берем с помощью байдинга
        val adapter = VpAdapter(activity as FragmentActivity, flist) /* создаем адаптер и присваеваем инстанцию,
    передаем фрагментактивити и список с объектами между которыми мы будем преключаться */
        vp.adapter = adapter // берем ID вьпейджера и присваем ему адаптер, который создали выше
        TabLayoutMediator(tabLayout,
            vp) {  // связываем таблаяут с вьюпейджером, для этого используем таблаяутмедиатр
                tab, pos ->
            tab.text =
                tList[pos] // сдесь происходит переключение tab - кнопка на кот нажали, pos - куда переключилось
        }.attach() // необходимый атрибут

    }
        private fun updateCurrentCard() = with(binding){ // это колбек который будет ждать результата
            model.liveDataCurrent.observe(viewLifecycleOwner){
                val maxMinTemt = "${it.maxTemp}C°/${it.minTemp}C°"
                tvData.text = it.time
                tvCity.text = it.city
                tvCureentTemp.text = it.currentTemp
                tvCondition.text = it.condition
                tvMaxMin.text = maxMinTemt
                Picasso.get().load("https:" + it.ImageUrl).into(imWeather) // передали картинки в библиотеку пикассо
            }
        }

    private fun permissionListener() { // проверка разрешения от пользователя
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun requestWeatherData(city: String) { // привязываемся к серверу
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY + // вместо IP ключа делаем константу const val API_KEY
                "&q=" +
                city + // вместо конкретного города указываем ссылку на город
                "&days=" +
                "3" +
                "&aqi=no&alerts=no"

        val queue =
            Volley.newRequestQueue(context)// создали очередь и добавили в нее контекст newRequestQueue - это наш запрос
        val reqest = StringRequest(
            Request.Method.GET,      // запрос на получение данных из библиотеки
            url, {                   // запрос к url
                    result ->
                parseWeatherData(result)           // слушатель результата
            },
            { error ->
                Log.d("MyLog", "Error: $error")            // слушатель ошибок- выдает ошибку
            }
        )                           // создали запрос к библиотеке
        queue.add(reqest)                 // передаем запрос в очередь

    }

    private fun parseWeatherData(result: String) { // основная функция в которой делаем парсинг с сайта
        val mainObject = JSONObject(result) // создали Джейсон обжект
        val list = parseDays(mainObject)
        parseCurrentData(mainObject, list[0])
    }

    private fun parseDays(mainObject: JSONObject): List<WeatherModel>{ // будет выдавать список из WeatherModel (для всех дней)
        val list = ArrayList<WeatherModel>() // создали пустой список
        val daysArray = mainObject.getJSONObject("forecast")
            .getJSONArray("forecastday") // берем массив с сайта
        val name = mainObject.getJSONObject("location").getString("name") // получаем джонсон объект для названия города он менятся не будет
        for (i in 0 until daysArray.length()) { // запускаем цикл для перебора массива - если будет пять дней - будет пять списков
            val day = daysArray[i] as JSONObject // передаем индекс массива в переменную
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()

            )
            list.add(item) // добавляем данные в список list
        }
        return list // возвращаем список
    }


    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel){ // в этой функции парсим данные для основной карточки (сегодняшнего дня)
        val item = WeatherModel( // передали в переменную ссылку на data class WeatherModel
            mainObject.getJSONObject("location").getString("name"), // получаем джонсон объект для названия города
            mainObject.getJSONObject("current").getString("last_updated"), // получаем джонсон объект для времени
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"), // получаем джонсон объект для температуры
            mainObject.getJSONObject("current").getString("temp_c"), // получаем джонсон объект для температуры
            weatherItem.maxTemp,
            weatherItem.minTemp,
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"), // получаем джонсон объект для картинки
            weatherItem.hours
        )
        model.liveDataCurrent.value = item
        Log.d("MyLog", "City: ${item.maxTemp}")            // слушатель ошибок- выдает ошибку
        Log.d("MyLog", "Time: ${item.minTemp}")            // слушатель ошибок- выдает ошибку
        Log.d("MyLog", "Time: ${item.hours}")            // слушатель ошибок- выдает ошибку

    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}