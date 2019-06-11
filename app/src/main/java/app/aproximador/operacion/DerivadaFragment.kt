package app.aproximador.operacion

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import app.aproximador.R
import app.aproximador.databinding.FragmentDerivadaBinding
import app.aproximador.util.CalculoDerivadaUtil
import app.aproximador.view.MyMarkerView
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.EntryXComparator
import kotlinx.android.synthetic.main.fragment_derivada.*
import java.util.*
import kotlin.collections.ArrayList


class DerivadaFragment : Fragment() {

    private val CALCULO_TAG: String = "CALCULO"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDerivadaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(viewParent: View, savedInstanceState: Bundle?) {
        super.onViewCreated(viewParent, savedInstanceState)

        // CONFIG GRAFICADOR
        grafico.setNoDataText(getString(R.string.mensaje_vacio))

        // CORRECCIONES PARA EL TEXTO HINT EN FOCO
        formulaInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.formula_hint) }
        comienzoXInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.comienzoX_hint) }
        comienzoYInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.comienzoY_hint) }
        puntoAInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoA_hint) }
        puntoBInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoB_hint) }
        incrementoInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.incremento_hint) }

        comienzoXInput.doAfterTextChanged { input: Editable? -> puntoAInput.text = input }
        incrementoInput.doAfterTextChanged { input: Editable? -> Log.i(CALCULO_TAG, input.toString()) }
        incrementoInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                realizarCalculo(
                    formulaInput.text.toString(),
                    comienzoXInput.text.toString().toFloat(),
                    comienzoYInput.text.toString().toFloat(),
                    incrementoInput.text.toString().toFloat(),
                    puntoAInput.text.toString().toFloat(),
                    puntoBInput.text.toString().toFloat()
                )
                true
            } else false
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
        valorX: Float,
        valorY: Float,
        incremento: Float,
        puntoA: Float,
        puntoB: Float
    ) {
        var calculoDerivada: CalculoDerivadaUtil
        var puntos: ArrayList<Entry>
        var sets: ArrayList<ILineDataSet> = ArrayList()
        var dataSet: LineDataSet

        calculoDerivada = CalculoDerivadaUtil(formula, valorX, valorY)
        puntos = calculoDerivada.obtenerPuntos(incremento, puntoA, puntoB)
        dataSet = LineDataSet(puntos, "Derivada+")
        dataSet.color = ColorTemplate.VORDIPLOM_COLORS[0]
        sets.add(dataSet)

        calculoDerivada = CalculoDerivadaUtil(formula, valorX, valorY)
        puntos = calculoDerivada.obtenerPuntosNegativo(incremento, puntoA, puntoB)
        Collections.sort(puntos, EntryXComparator())
        dataSet = LineDataSet(puntos, "Derivada-")
        dataSet.color = ColorTemplate.VORDIPLOM_COLORS[1]
        sets.add(dataSet)

        mostrarGrafico( sets )
    }

    private fun mostrarGrafico(dataSet: ArrayList<ILineDataSet>) {
        val lineData = LineData(dataSet)
        lineData.setDrawValues(false)

        grafico.data = lineData
        grafico.description.isEnabled = false
        grafico.legend.isEnabled = false
        grafico.animateXY(1000, 1000)

        val mv = MyMarkerView(requireActivity(), R.layout.custom_marker_view)
        mv.chartView = grafico
        grafico.marker = mv

        val xAxis = grafico.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.axisMaximum = 10f
        xAxis.axisMinimum = -10f

        val leftAxis = grafico.axisLeft
        leftAxis.isEnabled = true
        leftAxis.axisMaximum = 10f
        leftAxis.axisMinimum = -10f

        val rightAxis = grafico.axisRight
        rightAxis.isEnabled = false

        grafico.invalidate()
    }
}
