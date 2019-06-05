package app.aproximador

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import app.aproximador.util.CalculoUtil
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private val CALCULO_TAG: String = "CALCULO"
    private val STAR_EMOJI = '\u2B50'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // CONFIG EMOJI
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        // CORRECCIONES PARA EL TEXTO HINT EN FOCO
        formulaInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.formula_hint) }
        comienzoXInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.comienzoX_hint) }
        comienzoYInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.comienzoY_hint) }
        incrementoInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.incremento_hint) }
        puntoAInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoA_hint) }
        puntoBInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoB_hint) }

        // ACCION DEL BOTON
        fab.setOnClickListener {
            realizarCalculo(
                formulaInput.text.toString(),
                comienzoXInput.text.toString().toDouble(),
                comienzoYInput.text.toString().toDouble(),
                incrementoInput.text.toString().toDouble(),
                puntoAInput.text.toString().toDouble(),
                puntoBInput.text.toString().toDouble()
            )
        }
    }

    private fun fixHintEditText(view: EditText, b: Boolean, valor: Int) {
        var texto = ""
        if (b) {
            texto = getString(valor)
        }
        view.hint = texto
    }

    private fun realizarCalculo(
        formula: String,
        valorX: Double,
        valorY: Double,
        incremento: Double,
        puntoA: Double,
        puntoB: Double
    ) {
        val calculo = CalculoUtil(formula, valorX, valorY)

        Log.i(CALCULO_TAG, "Funcion: ${calculo.formula}")
        Log.i(CALCULO_TAG, "Syntax: ${calculo.checkSyntax()} - Expresion: ${calculo.expression()} - Resultado: ${calculo.calcular()}")

//        resultado.text = calculo.calcular().toString()
        mostrarGrafico(calculo, incremento, puntoA, puntoB)
    }

    private fun mostrarGrafico(
        calculo: CalculoUtil,
        h: Double,
        puntoA: Double,
        puntoB: Double
    ) {
        // DATA
        val points = calculo.obtenerPuntos(h, puntoA, puntoB)
        val arrayPoint = arrayOfNulls<DataPoint>(points.size)

        // CONFIGURACION
        val graph = findViewById<View>(R.id.grafico) as GraphView
        graph.removeAllSeries()

        val series = LineGraphSeries<DataPoint>(points.toArray(arrayPoint))
//        series.size = 10.0f

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(-100.0)
        graph.viewport.setMaxY(100.0)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(-100.0)
        graph.viewport.setMaxX(100.0)

        graph.viewport.isScalable= true
        graph.viewport.setScalableY(true)

        graph.addSeries(series)

    }

//    private fun armarArbolito() : String {
//        var arbol = ""
//        val rows = 10
//        for (i in 1..rows) {
//            var space = rows - i
//            while (space > 0) {
//                arbol += "   "
//                space--
//            }
//            var point = i
//            while (point > 0) {
//                arbol += "${STAR_EMOJI}  "
//                point--
//            }
//            arbol += "\n"
//        }
//        return arbol;
//    }
//
//    private fun mostrarArbolito(titulo: TextView) {
//        val tituloSecundario = findViewById<View>(R.id.titulo_secundario) as TextView
//        EmojiCompat.get().registerInitCallback(InitCallback(titulo, armarArbolito()))
//
//        val animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate_text)
//        val animation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_text)
//        animation1.reset()
//        animation2.reset()
//
//        titulo.clearAnimation()
//        titulo.startAnimation(animation2)
//        tituloSecundario.clearAnimation()
//        tituloSecundario.startAnimation(animation1)
//
//    }

    private class InitCallback internal constructor(
        regularTextView: TextView,
        mensaje: String
    ) : EmojiCompat.InitCallback() {

        private val mRegularTextViewRef: WeakReference<TextView> = WeakReference(regularTextView)
        private val texto : String = mensaje

        override fun onInitialized() {
            val regularTextView = mRegularTextViewRef.get()
            if (regularTextView != null) {
                val compat = EmojiCompat.get()
                val context = regularTextView.context
                regularTextView.text = compat.process(texto)
            }
        }
    }
}
