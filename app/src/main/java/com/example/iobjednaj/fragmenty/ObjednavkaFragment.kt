package com.example.iobjednaj.fragmenty
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.databinding.FragmentObjednavkaBinding
import com.example.iobjednaj.triedy.recyclerView_polozky.PokladnaViewModel
import com.example.iobjednaj.triedy.interfaces.IOnButtonClicked
import com.example.iobjednaj.triedy.recyclerView_polozky.MyAdapterPolozky
import com.example.iobjednaj.triedy.recyclerView_stoly.Stol



/**
 * ObjednavkaFragment je potomok [Fragment].
 * Tento fragment zobrazí obrazovku, kde možno vidieť jednotlivé položky v pokladni, ktoré si zákazník môže objednať.
 * Je možné ľubovoľne pridávať a uberať položky, suma sa automaticky bude prepočítavať.
 * Ďalej je možné vymazať danú objednávku, alebo ju zaplatiť v hotovosti alebo kartou, kde sa
 * výška tejto objednávky pripíše do pokladne.
 *
 * @property bind Predstavuje väzbový (binding) objekt pre layout Fragmentu.
 * @property binding Poskytuje prístup k vätbovému objektu pre pre layout Fragmentu.
 * @property recyclerView RecyclerView pre zobrazovanie zoznamu položiek v pokladni.
 * @property layoutManager Slúži pre správu (riadenie) rozloženia položiek v RecyclerView.
 * @property adapter Slúži na naplnenie položiek do recyclerView.
 * @property stol Predastuje daný stol, na ktorom sa vytvára objednávka.
 * @property pokladna Predstvuje pokladňu pre správu transkacií.
 */
class ObjednavkaFragment : Fragment(),
    IOnButtonClicked {

    private var bind: FragmentObjednavkaBinding? = null
    private val binding get() = bind!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: MyAdapterPolozky
    private lateinit var stol:Stol
    private lateinit var pokladna: PokladnaViewModel

    /**
     * Metóda, ktorá sa volá na vytvorenie hierarchie zobrazenia spojenej s fragmentom.
     * Vytvorí sa recycler view pre jednotlivé položky v pokladni, spolu s adaptérom pre ich manipuláciu.
     * Vytvoria sa príslušné listenery na dané tlačidla a metódy, ktoré sa majú vykonať pri ich stlačení.
     * Inicializuje sa pokladna.
     *
     * Ak sa do tohto fragmentu príde z fragmentu StolyFragment, tak sa kontroluje, či nejde o objednávku
     * daného stola, ktorý má už objednávku vytvorenú - aktualizuje sa obsah.
     *
     * Ak sa stlačí tlačidlo späť, tak sa užívateľ dostáva späť do fragmentu so stolmi.
     *
     * @param inflater Objekt LayoutInflater, ktorý možno použiť na nafúknutie ľubovoľného zobrazenia vo fragmente.
     * @param container Ak nie je null, toto je nadradené zobrazenie, ku ktorému by malo byť pripojené GUI fragmentu.
     * @param savedInstanceState Ak nie je null, tento fragment sa znovu vytvára z predchádzajúceho uloženého stavu.
     * @return Vráti pohľad na rozloženie fragmentu.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentObjednavkaBinding.inflate(inflater, container, false)
        pokladna = ViewModelProvider(requireActivity())[PokladnaViewModel::class.java]

        val args = arguments
        if (args != null) {
            this.stol = args.getSerializable("table") as Stol
            this.binding.stolObj.text = stol.dajNazovStola()
            prepocitajSumu()
        }

        recyclerView = binding.recyclerViewPolozky
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter =
            MyAdapterPolozky(requireContext(), stol.dajPolozky())
        recyclerView.adapter = adapter
        adapter.setOnButtonClickListener(this)

        val callBack: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val bundle = Bundle().apply{
                    putSerializable("upravenyStol", stol)
                }
                navController.previousBackStackEntry?.savedStateHandle?.set("upravenyStol", bundle)
                navController.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        binding.zmazatObj.setOnClickListener {
            if (stol.dajSumuUctu() != 0.0) {
                zobrazDialogVymazanie()
            } else {
                zobrazInfo()
            }
        }

        binding.zaplHotovost.setOnClickListener {
            if (stol.dajSumuUctu() != 0.0) {
                val titleText = "PLATBA V HOTOVOSTI"
                zobrazDialogZaplatenie(titleText, true)
            } else {
                zobrazInfo()
            }
        }

        binding.zaplKartou.setOnClickListener {
            if (stol.dajSumuUctu() != 0.0) {
                val titleText = "PLATBA KARTOU"
                zobrazDialogZaplatenie(titleText, false)
            } else {
                zobrazInfo()
            }
        }
        return binding.root
    }

    /**
     * Metóda, ktorá zobrazí krátku správu, pokiaľ nebola vytvorená objednávka a užívateľ chce zmazať objednávku
     * alebo ju zaplatiť.
     */
    private fun zobrazInfo() {
        Toast.makeText(requireContext(), "Nebola vytvorená objednávka.", Toast.LENGTH_SHORT).show()
    }

    /**
     * Metóda, ktorá slúži na prepočitanie sumy objednávky na danom stole.
     * Dôjde k aktualizácii obsahu zobrazovaného v TextView.
     */
    private fun prepocitajSumu() {
        stol.prepocitajSumuStola()
        val sumaStr:String = String.format("%.2f €", stol.dajSumuUctu())
        binding.sumaZobr.text = sumaStr
    }

    /**
     * Metóda, ktorá sa volá, keď užívateľ klikne na tlačidlo vymazať objednávku.
     * Slúži na anuláciu jednotlivých objednaných položiek.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun upravSumuAnuluj() {
        stol.anulujSumu()
        adapter.notifyDataSetChanged()
        prepocitajSumu()
    }

    /**
     * Metóda, ktorá slúži na zobrazenie dialogového okna, ak užívateľ klikne na tlačidlo vymazať objednávku.
     * Upozorní užívateľa, či naozaj chce anulovať objednané položky.
     * Buď užívateľ klikne na tlačidlo áno, a dôjde k anulácii, alebo zvolí nie a nič sa neanuluje.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun zobrazDialogVymazanie() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("ZMAZANIE OBJEDNÁVKY")
        alertDialogBuilder.setMessage("Naozaj si prajete vymazať objednané položky?")
        alertDialogBuilder.setPositiveButton("ÁNO") { dialog, _ ->
            stol.anulujSumu()
            adapter.notifyDataSetChanged()
            prepocitajSumu()
            Toast.makeText(requireContext(), "Objednávka bola zrušená.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("NIE") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    /**
     * Metóda, ktorá slúži na zobrazenie dialogového okna, ak užívateľ klikne na tlačidlo zaplatiť kartou
     * alebo v hotovosti.
     * Upozorní užívateľa, či naozaj chce zaplatiť daný účet na stole.
     * Buď užívateľ klikne na tlačidlo áno, a dôjde k zaplateniu a následnemu prepočtu do pokladne
     * a taktiež prechodu späť do StolyFragment, alebo zvolí nie a nič sa nestane.
     *
     * @param titleText Titulný názov dialogového okna.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun zobrazDialogZaplatenie(titleText: String, vHotovosti: Boolean) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(titleText)
        alertDialogBuilder.setMessage("Naozaj si prajete ukončiť objednávku a zaplatiť?")
        alertDialogBuilder.setPositiveButton("ÁNO") { dialog, _ ->
            val navController = findNavController()
            if (vHotovosti) {
                pokladna.platbaHotovost(stol.dajSumuUctu())
            } else {
                pokladna.platbaKartou(stol.dajSumuUctu())
            }
            upravSumuAnuluj()
            navController.popBackStack()
            dialog.dismiss()
            Toast.makeText(requireContext(), "Objednávka úspešne zaplatená.", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("NIE") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }


    /**
     * Metóda z interface IOnButtonClicked, ktorá slúži na interakciu, ak užívateľ klikne na tlačidlo +
     * príslušnej položky v recyclerView.
     * Následne sa volá metóda prepočitajSumu().
     */
    override fun onPridajClicked() {
        prepocitajSumu()
    }

    /**
     * Metóda z interface IOnButtonClicked, ktorá slúži na interakciu, ak užívateľ klikne na tlačidlo -
     * príslušnej položky v recyclerView.
     * Následne sa volá metóda prepočitajSumu().
     */
    override fun onUberClicked() {
        prepocitajSumu()
    }

    /**
     * Metóda, ktorá sa volá, keď už nie je potrebný pohľad na fragment, aby nedochádzalo k Memory Leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

}