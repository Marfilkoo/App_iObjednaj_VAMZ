package com.example.iobjednaj.fragmenty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.iobjednaj.R
import com.example.iobjednaj.databinding.FragmentUzavierkaBinding
import com.example.iobjednaj.triedy.recyclerView_polozky.PokladnaViewModel

/**
 * UzavierkaFragment je potomok [Fragment].
 * Tento fragment zobrazí obrazovku, kde možno vidieť denný prehľad - výšku denného vkladu, sumu
 * v hotovsti a v kartách a celkovú dennú sumu.
 * Slúži na vykonanie dennej uzávierky.
 *
 * @property bind Predstavuje väzbový (binding) objekt pre layout Fragmentu.
 * @property binding Poskytuje prístup k vätbovému objektu pre pre layout Fragmentu.
 * @property pokladna Predstvuje pokladňu pre správu transkacií.
 */
class UzavierkaFragment : Fragment() {

    private var bind: FragmentUzavierkaBinding? = null
    private val binding get() = bind!!
    private lateinit var pokladna: PokladnaViewModel

    /**
     * Metóda, ktorá "nafúkne", respektíve vytvorí rozloženie pre tento fragment a inicializuje sa aj ViewModel pre pokladnu.
     * Volá sa na vytvorenie hierarchie zobrazenia spojenej s fragmentom.
     * Zabezpečí, aby nebolo možné sa dostať späť kliknutím tlačidla späť v mobile.
     * Nastavia sa príslušné sumy a priradí sa listener na tlačidlo VYKONAŤ.
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
        bind = FragmentUzavierkaBinding.inflate(inflater, container, false)
        pokladna = ViewModelProvider(requireActivity())[PokladnaViewModel::class.java]

        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Nutné vykonať dennú uzávierku.", Toast.LENGTH_SHORT).show()
            }
        }
        nastavSumy()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        binding.vykonat.setOnClickListener {
            Toast.makeText(requireContext(), "Denná uzávierka bola vykonaná.", Toast.LENGTH_SHORT).show()
            pokladna.aktualizujPokladnu()

            findNavController().navigate(R.id.action_uzavierkaFragment_to_uvodnyFragment)

        }
        return binding.root
    }

    /**
     * Metóda, ktorá slúži na nastavenie jednotlivých súm do príslušných TextView.
     */
    private fun nastavSumy() {
        val hotovostSuma: String = String.format("%.2f €", pokladna.kolkoVhotovosti())
        val kartySuma: String = String.format("%.2f €", pokladna.kolkoKartkami())
        val celkom: String = String.format("%.2f €", pokladna.kolkoVpokladni())
        val vklad: String = String.format("%.2f €", pokladna.dajVklad())

        binding.denVkladSuma.text = vklad
        binding.sumaHotovost.text = hotovostSuma
        binding.sumaKarty.text = kartySuma
        binding.sumaSpolu.text = celkom
    }
}