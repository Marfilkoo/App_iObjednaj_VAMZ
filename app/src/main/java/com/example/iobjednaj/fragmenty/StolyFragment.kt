package com.example.iobjednaj.fragmenty

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.R
import com.example.iobjednaj.databinding.FragmentStolyBinding
import com.example.iobjednaj.triedy.recyclerView_polozky.PokladnaViewModel
import com.example.iobjednaj.triedy.recyclerView_stoly.MyAdapterStoly
import com.example.iobjednaj.triedy.interfaces.IRecyclerViewInterface
import com.example.iobjednaj.triedy.recyclerView_stoly.Stol
import com.example.iobjednaj.triedy.recyclerView_stoly.StolyViewModel

/**
 * StolyFragment je potomok [Fragment].
 * Tento fragment zobrazí obrazovku, kde možno vidieť jednotlivé stoly spolu s ich výškou účtov,
 * ak je na nich vytvorený účet.
 *
 * @property bind Predstavuje väzbový (binding) objekt pre layout Fragmentu.
 * @property binding Poskytuje prístup k vätbovému objektu pre pre layout Fragmentu.
 * @property recyclerView RecyclerView pre zobrazovanie zoznamu stolov.
 * @property layoutManager Slúži pre správu (riadenie) rozloženia stolov v RecyclerView.
 * @property adapter Slúži na naplnenie stolov do recyclerView.
 * @property zoznam Predastuje zoznam stolov, ktoré sa majú zobraziť v recyclerView.
 * @property pokladna Predstvuje pokladňu pre správu transkacií.
 */
class StolyFragment : Fragment(),
    IRecyclerViewInterface {

    private var bind: FragmentStolyBinding? = null
    private val binding get() = bind!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var zoznam: StolyViewModel
    private lateinit var adapter: MyAdapterStoly
    private lateinit var pokladna: PokladnaViewModel


    /**
     * Metóda, ktorá sa volá na vytvorenie hierarchie zobrazenia spojenej s fragmentom.
     * Vytvorí sa recycler view pre jednotlivé stoly, spolu s adaptérom pre ich manipuláciu.
     * Inicializuje sa pokladna a nastavia sa sumy.
     * Dôjde k obmedzeniu, kde nemožno sa vrátiť späť kliknutím tlačidlom späť.
     * Vytvorí sa príslušný listener na tlačidlo dennej uzávierky.
     *
     * Ak sa do tohto fragmentu príde z fragmentu ObjednavkaFragment, tak sa kontroluje, či nedošlo k nejakej zmene.
     * Ak došlo k zmene, tak sa aktualiuje daný obsah.
     *
     * @param inflater Objekt LayoutInflater, ktorý možno použiť na nafúknutie ľubovoľného zobrazenia vo fragmente.
     * @param container Ak nie je null, toto je nadradené zobrazenie, ku ktorému by malo byť pripojené GUI fragmentu.
     * @param savedInstanceState Ak nie je null, tento fragment sa znovu vytvára z predchádzajúceho uloženého stavu.
     * @return Vráti pohľad na rozloženie fragmentu.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentStolyBinding.inflate(inflater, container, false)

        zoznam = ViewModelProvider(requireActivity())[StolyViewModel::class.java]

        recyclerView = binding.myRecyclerView
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter =
            MyAdapterStoly(zoznam, requireContext(), this)
        recyclerView.adapter = adapter
        pokladna = ViewModelProvider(requireActivity())[PokladnaViewModel::class.java]

        nastavSumy()

        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), "Denný vklad bol už zadaný.", Toast.LENGTH_SHORT).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        binding.uzavierka.setOnClickListener {
            var otvoreneUcty = false
            for (stol in zoznam.dajStoly()) {
                if (stol.dajSumuUctu() != 0.0) {
                    otvoreneUcty = true
                    break
                }
            }
            if (otvoreneUcty) {
                zobrazDialogOtvoreneUcty()
            } else {
                zobrazDialogUzavierky()
            }
        }

        val navController = findNavController()
        val upravenyStol = navController.currentBackStackEntry?.savedStateHandle?.get<Bundle>("upravenyStol")?.getSerializable("upravenyStol") as? Stol
        upravenyStol?.let {
            for (stol in zoznam.dajStoly()) {
                if (stol.dajNazovStola() == upravenyStol.dajNazovStola()) {
                    val index = zoznam.dajStoly().indexOf(stol)
                    if (index != -1) {
                        zoznam.dajKonkretnyStol(index)?.upravObjednanePolozky(upravenyStol)
                    }
                }
            }
        }
        val suma = navController.previousBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("suma")
        val operacia = navController.previousBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("operacia")

        suma?.observe(viewLifecycleOwner) { sumaBundle ->
            val sumaNaPripocitanie = sumaBundle?.getDouble("suma")
            suma.let {
                if (operacia?.value?.getChar("operacia") == 'H') {
                    if (sumaNaPripocitanie != null) {
                        pokladna.platbaHotovost(sumaNaPripocitanie)
                    }
                } else if (operacia?.value?.getChar("operacia") == 'K') {
                    if (sumaNaPripocitanie != null) {
                        pokladna.platbaKartou(sumaNaPripocitanie)
                    }
                }
                this.nastavSumy()
            }
        }
        return binding.root
    }

    /**
     * Metóda, ktorá sa volá akonáhle užívateľ stlačí na tlačidlo dennej uzávierky, ale v zozname stolov
     * sú nevyplatené účty.
     */
    private fun zobrazDialogOtvoreneUcty() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("OTVORENÉ ÚČTY")
        alertDialogBuilder.setMessage("V systéme sú evidované otvorené (nevyplatené) účty.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    /**
     * Metóda, ktorá sa volá, keď užívateľ stlačí tlačidlo dennej uzávierky a v zozname, nie je žiadny otvorený účet.
     * Ide o zobrazenie dialogového okna, aby sa užívateľ naozaj uistil, či chce vykonať dennú
     * uzávierku. Akonáhle stlačí voľbu áno - zobrazí sa fragmernt UzavierkaFragment.
     */
    private fun zobrazDialogUzavierky() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Prajete si vykonať DENNÚ UZÁVIERKU?")
        alertDialogBuilder.setPositiveButton("ÁNO") { dialog, _ ->
            findNavController().navigate(R.id.action_stolyFragment_to_uzavierkaFragment)
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("NIE") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    /**
     * Metóda, ktorá slúži na nastavenie jednotlivých súm v prehľade - výška denného vkladu, jednotlivé transakcie
     * v hotovosti alebo v kartách a sumu celkom.
     */
    private fun nastavSumy() {
        val hotovostSuma: String = String.format("%.2f €", pokladna.kolkoVhotovosti())
        val kartySuma: String = String.format("%.2f €", pokladna.kolkoKartkami())
        val celkom: String = String.format("%.2f €", pokladna.kolkoVpokladni())
        val vklad: String = String.format("%.2f €", pokladna.dajVklad())

        binding.sumaVklad.text = vklad
        binding.hotovstSuma.text = hotovostSuma
        binding.kartySuma.text = kartySuma
        binding.spoluTransakcie.text = celkom
    }

    /**
     * Metóda z interface RecyclerViewInterface, ktorá zabezpečí interakciu, ak užívateľ klikne na príslušný
     * stôl v recyclerView so stolmi.
     * Následne sa užívateľ po kliknutí na daný stol dostáva do objednávkového fragmentu.
     */
    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("table", zoznam.dajKonkretnyStol(position))
        findNavController().navigate(R.id.action_stolyFragment_to_objednavkaFragment, bundle)
    }

    /**
     * Metóda, ktorá sa volá, keď už nie je potrebný pohľad na fragment, aby nedochádzalo k Memory Leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }
}