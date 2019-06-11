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
import app.aproximador.databinding.FragmentIntegralBinding
import app.aproximador.util.CalculoIntegralUtil
import app.aproximador.view.MyMarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_integral.*

class IntegralFragment : Fragment() {

    private val CALCULO_TAG: String = "CALCULO"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIntegralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(viewParent: View, savedInstanceState: Bundle?) {
        super.onViewCreated(viewParent, savedInstanceState)

        // CONFIG GRAFICADOR
        grafico.setNoDataText(getString(R.string.mensaje_vacio))

        // CORRECCIONES PARA EL TEXTO HINT EN FOCO
        formulaInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.formula_integra_hint) }
        puntoAInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoA_hint) }
        puntoBInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoB_hint) }
        intervaloInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.intervalo_hint) }

        intervaloInput.doAfterTextChanged { input: Editable? -> Log.i(CALCULO_TAG, input.toString()) }
        intervaloInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                realizarCalculo(
                    formulaInput.text.toString(),
                    intervaloInput.text.toString().toInt(),
                    puntoAInput.text.toString().toFloat(),
                    puntoBInput.text.toString().toFloat()
                )
                true
            } else false
        }
    }

    private fun realizarCalculo(
        formula: String,
        intervalo: Int,
        puntoA: Float,
        puntoB: Float
    ) {
        var sets: ArrayList<IBarDataSet> = ArrayList()
        var dataSet: BarDataSet
        var puntos: ArrayList<BarEntry>

        val calculoIntegral = CalculoIntegralUtil(formula, puntoA, puntoB, intervalo)
        puntos = calculoIntegral.obtenerPuntos()
        dataSet = BarDataSet(puntos, "Integrada")
        sets.add(dataSet)

        mostrarGrafico(sets, calculoIntegral)
    }

    private fun mostrarGrafico(
        dataSet: ArrayList<IBarDataSet>,
        calculoIntegral: CalculoIntegralUtil
    ) {
        val data = BarData(dataSet)
        data.setDrawValues(false)
        data.barWidth = calculoIntegral.amplitud

        grafico.data = data
        grafico.description.text = "Aprox: ${calculoIntegral.aproximacion}"
        grafico.legend.isEnabled = false
        grafico.animateXY(1000, 1000)
        grafico.setFitBars(true)

        val mv = MyMarkerView(requireActivity(), R.layout.custom_marker_view)
        mv.chartView = grafico
        grafico.marker = mv

        val xAxis = grafico.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.axisMaximum = 10f
        xAxis.axisMinimum = 0f

        val leftAxis = grafico.axisLeft
        leftAxis.isEnabled = true
        leftAxis.axisMaximum = 10f
        leftAxis.axisMinimum = -10f

        val rightAxis = grafico.axisRight
        rightAxis.isEnabled = false


        grafico.invalidate()
    }

    private fun fixHintEditText(view: EditText, b: Boolean, valor: Int) {
        var texto = ""
        if (b) {
            texto = getString(valor)
        }
        view.hint = texto
    }
}
