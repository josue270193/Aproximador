package app.aproximador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.aproximador.databinding.FragmentAcercaBinding

class AcercaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAcercaBinding.inflate(inflater, container, false)
        return binding.root
    }

}
