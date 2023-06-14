package com.example.iobjednaj.triedy.recyclerView_polozky

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.R
import com.example.iobjednaj.triedy.interfaces.IOnButtonClicked

/**
 * Adaptér pre správu (riadenie) a zobrazenie zoznamu položiek v RecyclerView.
 *
 * @param context Kontext aktivity alebo fragmentu.
 * @param polozkyVpokladni Zoznam položiek, ktoré sa majú zobraziť.
 * @property buttonClickListener Objekt implementujúci interface na spracovanie interakcií s položkami v RecyclerView.
 */
class MyAdapterPolozky(
    private val context: Context,
    private val polozkyVpokladni: ArrayList<PolozkaVpokladni>
) : RecyclerView.Adapter<MyViewHolderPolozky>() {

    private var buttonClickListener: IOnButtonClicked? = null

    /**
     * Metóda, ktorá zabezpečí nastavenie príslušného Click listenera, aby bolo možné robiť úpravy
     * množstva objednanych položiek na daný stôl, respektíve objednávku.
     *
     * @param listener Listener (prijímač), ktorý sa má nastaviť.
     */
    fun setOnButtonClickListener(listener: IOnButtonClicked) {
        this.buttonClickListener = listener
    }

    /**
     * Metóda, ktorá vytvorí novú inštanciu MyViewHolderPolozky pre zobrazenie položky.
     *
     * @param parent Rodičovská skupina ViewGroup.
     * @param viewType Typ zobrazenia.
     * @return Vytvorená inštancia MyViewHolderPolozky.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderPolozky {
        val view = LayoutInflater.from(context).inflate(R.layout.polozka_item, parent, false)
        return MyViewHolderPolozky(view)
    }

    /**
     * Metóda, ktorá viaže údaje (položky) k ViewHolder.
     * Pre jednotlivé položky vytvorí aj listenery, aby bolo možné vykonávať interakcie, keď užívateľ
     * klikne na tlačidlo pridania (+) alebo odbratia (-) objednaného množstva.
     *
     * @param holder Inštancia MyViewHolderPolozky.
     * @param position Pozícia položky v zozname.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolderPolozky, position: Int) {
        val polozkaVpokladni = polozkyVpokladni[position]
        holder.nazovPolozky.text = polozkaVpokladni.dajNazovPolozky()
        val pocet = "${polozkaVpokladni.dajPocetObjednanych()}"
        holder.mnozstvo.text = pocet


        holder.pridaj.setOnClickListener {
            polozkaVpokladni.zvysObjednanie()
            notifyDataSetChanged()
            buttonClickListener?.onPridajClicked()
        }

        holder.uber.setOnClickListener {
            polozkaVpokladni.znizObjednanie()
            notifyDataSetChanged()
            buttonClickListener?.onUberClicked()
        }
    }

    /**
     * Metóda, ktorá vráti počet položiek v zozname.
     *
     * @return Počet položiek.
     */
    override fun getItemCount(): Int {
        return polozkyVpokladni.size
    }
}