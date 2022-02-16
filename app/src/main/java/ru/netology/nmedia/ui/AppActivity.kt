package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


     //   A: мы хотим, чтобы StatsView принимала на вход данные, по которым сама рассчитывала проценты:

        findViewById<StatsView>(R.id.stats).data = listOf(
            500F,
            500F,
            500F,
            500F,
        )

    //        Далее нам потребуется немного математики, чтобы рисовать «дуги»:
//         25% = 3600 * 0.25 = 900 (угол рисуем с права на лево ( пример -часы с 15 на 12 ))
//        findViewById<StatsView>(R.id.stats).data = listOf(
//            0.25F,
//            0.25F,
//            0.25F,
//            0.25F,
//        )







    }
}