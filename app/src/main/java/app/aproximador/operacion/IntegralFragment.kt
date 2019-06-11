package app.aproximador.operacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.aproximador.databinding.FragmentIntegralBinding

class IntegralFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIntegralBinding.inflate(inflater, container, false)

        return binding.root
    }

}
