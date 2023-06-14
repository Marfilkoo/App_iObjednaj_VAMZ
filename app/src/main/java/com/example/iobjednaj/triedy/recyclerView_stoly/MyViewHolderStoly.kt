package com.example.iobjednaj.triedy.recyclerView_stoly

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.R
import com.example.iobjednaj.triedy.interfaces.IRecyclerViewInterface

/**
 * Trieda, ktorá slúži na zobrazovanie jednotlivých stolov v Recycler View.
 *
 * @param itemView pohľad na rozloženie daného stola.
 * @param IRecyclerViewInterface Objekt implementujúci interface na spracovanie interakcií so stolmi v RecyclerView.
 */
class MyViewHolderStoly (itemView: View, private val IRecyclerViewInterface: IRecyclerViewInterface) : RecyclerView.ViewHolder(itemView) {

    /**
     * AppCompatButton pre zobrazenie názvu daného stola v RecyclerView.
     */
    val nazovStola: AppCompatButton = itemView.findViewById(R.id.nazov_stola)

    /**
     * TextView pre zobrazenie sumy daného stola v RecyclerView.
     */
    val sumaStola: TextView = itemView.findViewById(R.id.suma_stola)

    /**
     * Inicializačný blok, ktorý slúži na nastavenie listenera na tlačidlo s názvom stola.
     */
    init {
        nazovStola.setOnClickListener {
            IRecyclerViewInterface.onItemClick(adapterPosition)
        }
    }
}