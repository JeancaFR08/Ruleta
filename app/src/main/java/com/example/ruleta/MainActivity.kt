package com.example.ruleta

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.util.Random

class MainActivity : AppCompatActivity() {

    val sectores = intArrayOf(5, 20, 5, 20, 5, 20, 5, 20)
    // Probabilidades de los sectores (ajusta según tus necesidades)
    val sectorDegrees = IntArray(sectores.size)

    var randomSectorIndex = 0

    var wheel: ImageView? = null
    var spinning = false
    var earningsRecord = 0

    val random = Random()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wheel = findViewById(R.id.wheel)
        val belt = findViewById<ImageView>(R.id.belt)

        generateSectorDegrees()

        belt.setOnClickListener {
            if (!spinning) {
                spin()
                spinning = true
            }
        }

        val withdrawBtn = findViewById<Button>(R.id.withdrawBtn)
        val text = "Listo para intercambiar tu premio $earningsRecord"
    }

    private fun spin() {
        randomSectorIndex = selectRandomSectorIndex()

        val randomDegree = generateRandomDegreeToSpinTo(randomSectorIndex)

        val rotateAnimation = RotateAnimation(0f, randomDegree.toFloat(),
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)

        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()

        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Implementación del método onAnimationStart
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Implementación del método onAnimationEnd
                val earnedCoins = sectores[randomSectorIndex]
                saveEarnings(earnedCoins)
                var txt = ""

                if (earnedCoins == 1) {
                    txt = "GRACIAS"
                } else if (earnedCoins == 2) {
                    txt = "PREMIO"
                }

                val sms = "Has ganado $txt"
                showToast(txt)

                val tv = findViewById<TextView>(R.id.earnings)
                tv.text = txt

                spinning = false
            }

            private fun showToast(txt: String) {
                Toast.makeText(applicationContext, txt, Toast.LENGTH_SHORT).show()
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Implementación del método onAnimationRepeat
            }
        })

        wheel?.startAnimation(rotateAnimation)
    }

    private fun saveEarnings(earnedCoins: Int) {
        // Implementa el manejo de ganancias aquí
        earningsRecord += earnedCoins
    }

    private fun generateRandomDegreeToSpinTo(sectorIndex: Int): Int {
        return (360 + sectores.size) + sectorDegrees[sectorIndex]
    }

    private fun generateSectorDegrees() {
        val sectorDegree = 360 / sectores.size

        for (i in 0 until sectores.size) {
            sectorDegrees[i] = (i + 1) * sectorDegree
        }
    }

    private fun selectRandomSectorIndex(): Int {
        val randomValue = random.nextInt(100) // Número aleatorio entre 0 y 99

        var accumulator = 0
        for (i in sectores.indices) {
            accumulator += sectores[i]
            if (randomValue < accumulator) {
                return i
            }
        }

        // En caso de que no se seleccione ningún sector (esto no debería ocurrir si las probabilidades suman 100)
        return 0
    }
}