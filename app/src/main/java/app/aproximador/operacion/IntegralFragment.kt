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
        formulaInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.formula_hint) }
        puntoAInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoA_hint) }
        puntoBInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.puntoB_hint) }
        incrementoInput.setOnFocusChangeListener { view, b -> fixHintEditText(view as EditText, b, R.string.incremento_hint) }

        incrementoInput.doAfterTextChanged { input: Editable? -> Log.i(CALCULO_TAG, input.toString()) }
        incrementoInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                realizarCalculo(
//                    formulaInput.text.toString(),
//                    comienzoXInput.text.toString().toFloat(),
//                    comienzoYInput.text.toString().toFloat(),
//                    incrementoInput.text.toString().toFloat(),
//                    puntoAInput.text.toString().toFloat(),
//                    puntoBInput.text.toString().toFloat()
//                )
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
}
