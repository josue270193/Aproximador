package app.aproximador.util

import android.annotation.SuppressLint
import android.util.Log
import com.github.mikephil.charting.data.BarEntry
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

@SuppressLint("DefaultLocale")
class CalculoIntegralUtil(
    _formula: String,
    _valorA: Float,
    _valorB: Float,
    _intervalo: Int
) {

    private val INTEGRAL_TAG: String = "INTEGRAL"

    var formula: String = _formula.toLowerCase()
        set(value) { field = value.toLowerCase() }
    var valorA: Float = _valorA
    var valorB: Float = _valorB
    var intervalo: Int = _intervalo

    private var funcion: Function
    private lateinit var expresion: Expression
    var aproximacion: Float = 0.0f
    var amplitud: Float = 0.0f

    init {
        funcion = Function("f(x) = $formula")
    }

    fun checkSyntax(): Boolean {
        return expresion.checkSyntax()
    }

    fun expression(): String {
        return funcion.functionExpressionString
    }

    fun calcular(valorX: Float): Float {
        expresion = Expression("f($valorX)", funcion)
        return expresion.calculate().toFloat()
    }

    fun obtenerPuntos(): ArrayList<BarEntry> {
        Log.i(INTEGRAL_TAG, "Funcion: ${formula}")
        val puntos = ArrayList<BarEntry>()

        val valorIncremento = 0.5f
        val valorXo = 0f
        amplitud = (valorB - valorA) / intervalo

        for (k in 0 until intervalo) {
            val valorXk = valorXo + (k + valorIncremento) * amplitud
            val valorFxk = calcular(valorXk)
            aproximacion += amplitud * valorFxk
            puntos.add(BarEntry(valorXk, valorFxk))

            Log.i(INTEGRAL_TAG, "Syntax: ${checkSyntax()} - Expresion: ${expression()} - Resultado: ${calcular(valorXk)}")
            Log.i(INTEGRAL_TAG, "Punto calculado: ${valorXk} - F(xk)= ${valorFxk}" )
        }
        Log.i(INTEGRAL_TAG, "Aproximacion: ${aproximacion}" )
        return puntos
    }

}
