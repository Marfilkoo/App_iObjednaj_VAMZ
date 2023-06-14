package com.example.iobjednaj.triedy.recyclerView_stoly

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.R
import com.example.iobjednaj.triedy.interfaces.IRecyclerViewInterface

/**
 * Adaptér pre správu (riadenie) a zobrazenie zoznamu stolov v RecyclerView.
 *
 * @param zoznam Zoznam stolov, ktoré sa majú zobraziť.
 * @param context Kontext aktivity alebo fragmentu.
 * @param IRecyclerViewInterface Objekt implementujúci interface na spracovanie interakcií so stolmi v RecyclerView.
 */
class MyAdapterStoly(
    private val zoznam: StolyViewModel,
    private var context: Context,
    private val IRecyclerViewInterface: IRecyclerViewInterface
    ) : RecyclerView.Adapter<MyViewHolderStoly>() {

    /**
     * Metóda, ktorá vytvorí novú inštanciu MyViewHolderStoly pre zobrazenie položky.
     *
     * @param parent Rodičovská skupina ViewGroup.
     * @param viewType Typ zobrazenia.
     * @return Vytvorená inštancia MyViewHolderPolozky.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderStoly {
        val view = LayoutInflater.from(context).inflate(R.layout.table_item, parent, false)
        return MyViewHolderStoly(view, IRecyclerViewInterface)
    }

    /**
     * Metóda, ktorá viaže údaje (stoly) k ViewHolder.
     * Dochádza tu k nastavovaniu názvu daného stola a aj jeho suma, ak má vytvorený účet.
     *
     * @param holder Inštancia ViewHolder.
     * @param position Pozícia stola v zozname.
     */
    override fun onBindViewHolder(holder: MyViewHolderStoly, position: Int) {
        val stol = zoznam.dajKonkretnyStol(position)
        holder.nazovStola.text = stol!!.dajNazovStola()
        val suma = String.format("%.2f €", stol.dajSumuUctu())
        holder.sumaStola.text = suma
    }

    /**
     * Metóda, ktorá vráti počet stolov v zozname.
     *
     * @return Počet stolov.
     */
    override fun getItemCount(): Int {
        return zoznam.dajStoly().size
    }
}