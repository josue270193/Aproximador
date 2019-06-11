package app.aproximador.util

import android.annotation.SuppressLint
import com.github.mikephil.charting.data.Entry
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

@SuppressLint("DefaultLocale")
class CalculoUtil(_formula: String, _valorX: Float, _valorY: Float) {

    var formula: String = _formula.toLowerCase()
        set(value) { field = value.toLowerCase() }
    var valorX: Float = _valorX
    var valorY: Float = _valorY

    private var funcion: Function
    private var expresion: Expression

    init {
        funcion = Function("f(x,y) = $formula")
        expresion = Expression("f($valorX, ${valorY})", funcion)
    }

    fun checkSyntax(): Boolean {
        return expresion.checkSyntax()
    }

    fun expression(): String {
        return funcion.functionExpressionString
    }

    fun calcular(): Float {
        expresion = Expression("f($valorX, ${valorY})", funcion)
        return expresion.calculate().toFloat()
    }

    fun obtenerPuntos(h: Float, _puntoA: Float, _puntoB: Float) : ArrayList<Entry> {
        val lista = arrayListOf<Entry>()

        var puntoA = _puntoA
        var puntoB = _puntoB
        var puntoX = valorX
        var puntoY = valorY

        while (puntoA <= puntoB) {
            while (valorX < puntoA) {
                valorY += h * calcular()
                valorX += h
            }
            val coordenada = Entry(puntoA, valorY)
            lista.add(coordenada)
            puntoA += h
        }

        return lista
    }

    fun obtenerPuntosNegativo(h: Float, _puntoA: Float, _puntoB: Float) : ArrayList<Entry> {
        val lista = arrayListOf<Entry>()

        var puntoA = _puntoA
        var puntoB = _puntoB
        var puntoX = valorX
        var puntoY = valorY

        while (puntoA >= -puntoB) {
            while (valorX > puntoA) {
                valorY -= h * calcular()
                valorX -= h
            }
            val coordenada = Entry(puntoA, valorY)
            lista.add(coordenada)
            puntoA -= h
        }

        return lista
    }
}
