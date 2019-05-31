package app.aproximador

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    private val STAR_EMOJI = '\u2B50'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // CONFIG EMOJI
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        val titulo = findViewById<View>(R.id.titulo_inicio) as TextView
        fab.setOnClickListener {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show()
            realizarCalculo()
            mostrarGrafico()
            mostrarArbolito(titulo)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun realizarCalculo() {
        val funcion: Function = Function("f(a,b) = a + b")
        val expresion: Expression = Expression("f(1,4)", funcion)
        Log.i("CALCULO", "Syntax: " + expresion.checkSyntax() + " - Expresion: " + expresion.expressionString + " - Resultado: " + expresion.calculate())
    }

    private fun mostrarGrafico() {
        // DATA
        val points = arrayOfNulls<DataPoint>(50)
        for (i in 0..49) {
            points[i] = DataPoint(i.toDouble(), Math.sin(i * 0.5) * 20.0 * (Math.random() * 10 + 1))
        }

        // CONFIGURACION
        val graph = findViewById<View>(R.id.grafico) as GraphView

        val series = LineGraphSeries<DataPoint>(points)
        graph.addSeries(series)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.isYAxisBoundsManual = true

        graph.viewport.setMinY(-150.0)
        graph.viewport.setMaxY(150.0)
        graph.viewport.setMinY(4.0)
        graph.viewport.setMaxY(80.0)

        graph.viewport.isScalable = true
        graph.viewport.setScalableY(true)

    }

    private fun armarArbolito() : String {
        var arbol = ""
        val rows = 10
        for (i in 1..rows) {
            var space = rows - i
            while (space > 0) {
                arbol += "   "
                space--
            }
            var point = i
            while (point > 0) {
                arbol += "${STAR_EMOJI}  "
                point--
            }
            arbol += "\n"
        }
        return arbol;
    }

    private fun mostrarArbolito(titulo: TextView) {
        val tituloSecundario = findViewById<View>(R.id.titulo_secundario) as TextView
        EmojiCompat.get().registerInitCallback(InitCallback(titulo, armarArbolito()))

        val animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate_text)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_text)
        animation1.reset()
        animation2.reset()

        titulo.clearAnimation()
        titulo.startAnimation(animation2)
        tituloSecundario.clearAnimation()
        tituloSecundario.startAnimation(animation1)

    }

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
