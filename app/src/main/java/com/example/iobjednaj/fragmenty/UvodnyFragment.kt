package com.example.iobjednaj.fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.iobjednaj.R
import com.example.iobjednaj.databinding.FragmentUvodnyBinding
import com.example.iobjednaj.triedy.recyclerView_polozky.PokladnaViewModel

/**
 *
 * Uvodný fragment je potomok [Fragment].
 * Tento fragment zobrazí úvodnú obrazovku, kde je nutné zadať denný vklad.
 *
 * @property bind Predstavuje väzbový (binding) objekt pre layout Fragmentu.
 * @property binding Poskytuje prístup k vätbovému objektu pre pre layout Fragmentu.
 * @property pokladna Predstvuje pokladňu pre správu transkacií.
 */
class UvodnyFragment : Fragment() {
    private var bind: FragmentUvodnyBinding? = null
    private val binding get() = bind!!
    private lateinit var pokladna: PokladnaViewModel

    /**
     * Metóda, ktorá "nafúkne", respektíve vytvorí rozloženie pre tento fragment a inicializuje sa aj ViewModel pre pokladnu.
     * Volá sa na vytvorenie hierarchie zobrazenia spojenej s fragmentom.
     *
     * @param inflater Objekt LayoutInflater, ktorý možno použiť na nafúknutie ľubovoľného zobrazenia vo fragmente.
     * @param container Ak nie je null, toto je nadradené zobrazenie, ku ktorému by malo byť pripojené GUI fragmentu.
     * @param savedInstanceState Ak nie je null, tento fragment sa znovu vytvára z predchádzajúceho uloženého stavu.
     * @return Vráti pohľad na rozloženie fragmentu
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentUvodnyBinding.inflate(inflater, container, false)
        pokladna = ViewModelProvider(requireActivity())[PokladnaViewModel::class.java]
        return binding.root
    }

    /**
     * Metóda, ktorá sa volá hneď po vytvorení zobrazenia.
     * Slúži na vytvorenie listenera na tlačidlo POTVRDIŤ.
     * Následne sa skontroluje, či užívateľ zadal platný vklad, ak áno, je možné prejsť do ďalšieho fragmentu.
     *
     * @param view Vytvorený pohľad.
     * @param SaveInstanceState Ak nie je null, tento fragment sa znovu vytvára z predchádzajúceho uloženého stavu.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.potvrdit.setOnClickListener{
            val vkladStr = binding.vkladHodnota.text.toString()
            val vklad = vkladStr.toDoubleOrNull()

            if (vklad == null) {
                Toast.makeText(requireContext(), "Nutné zadať výšku denného vkladu.", Toast.LENGTH_SHORT).show()
            } else if (vklad < 0) {
                Toast.makeText(requireContext(), "Denny vklad nesmie byť záporný.", Toast.LENGTH_SHORT).show()
            } else {
                pokladna.nastavDennyVklad(vklad)
                findNavController().navigate(R.id.action_uvodnyFragment_to_stolyFragment)
            }
        }
    }

    /**
     * Metóda, ktorá sa volá, keď už nie je potrebný pohľad na fragment, aby nedochádzalo k Memory Leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }
}