package app.aproximador.util

import com.jjoe64.graphview.series.DataPoint
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function


class CalculoUtil(_formula: String, _valorX: Double, _valorY: Double) {

    var formula: String = _formula.toLowerCase()
        set(value) { field = value.toLowerCase() }
    var valorX: Double = _valorX
    var valorY: Double = _valorY

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

    fun calcular(): Double {
        expresion = Expression("f($valorX, ${valorY})", funcion)
        return expresion.calculate()
    }

    fun obtenerPuntos(h: Double, _puntoA: Double, puntoB: Double) : ArrayList<DataPoint> {
        val lista = arrayListOf<DataPoint>()

        var dy_dx: Double
        var delta_y: Double
        var puntoA = _puntoA

         while (puntoA <= puntoB){
             var temp = -0.0
             while (valorX < puntoA) {
                 temp = valorY
                 valorY += h * calcular()
                 valorX += h
             }
             val coordenada = DataPoint(puntoA, valorY)
             lista.add(coordenada)
             puntoA += h
        }
        return lista
    }

    fun findDeltaY(dy_dx: Double, incremento: Double): Double {
        return dy_dx * incremento
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        var x: Float
//        var y: Float
//        val m: Float
//        val i: Float
//        var dy_dx: Float
//        var delta_y: Float
//        x = input("Enter value for x: ")
//        y = input("Enter value for f(x): ")
//        m = input("Approximate what? [f(x)] : ")
//        i = input("Enter increment: ")
//
//        while (x <= m) {
//            dy_dx = F(x, y)
//            delta_y = find_delta_y(dy_dx, i)
//            System.out.printf("x: %.6f\ty: %.6f\tdy/dx: %.6f\tΔy: %.6f%n", x, y, dy_dx, delta_y)
//            y += delta_y
//            x += i
//        }
//    }
//
//    fun input(message: String): Float {
//        val number: Float
//        print(message)
//        number = java.util.Scanner(System.`in`).nextFloat()
//        return number
//    }
//
//    fun F(x: Float, y: Float): Float {
//        return 2 * x + 2 * y
//    }
//
}
