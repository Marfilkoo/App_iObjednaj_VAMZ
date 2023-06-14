package com.example.iobjednaj.triedy.recyclerView_polozky

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.iobjednaj.R

/**
 * Trieda, ktorá slúži na zobrazovanie jednotlivých položiek v Recycler View.
 *
 * @property itemView pohľad na rozloženie danej položky.
 */
class MyViewHolderPolozky(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * TextView pre zobrazenie názvu danej položky v RecyclerView.
     */
    val nazovPolozky: TextView = itemView.findViewById(R.id.nazov_polozky)

    /**
     * TextView pre zobrazenie objednaného množstva danej položky v RecyclerView.
     */
    val mnozstvo: TextView = itemView.findViewById(R.id.pocet)

    /**
     * AppCompatButton pre zvýšenie objednaného množstva danej položky.
     */
    val pridaj: AppCompatButton = itemView.findViewById(R.id.pridaj)

    /**
     * AppCompatButton pre zníženie objednaného množstva danej položky.
     */
    val uber: AppCompatButton = itemView.findViewById(R.id.uber)
}