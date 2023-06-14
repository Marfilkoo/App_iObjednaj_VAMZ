package com.example.iobjednaj.triedy.recyclerView_stoly

import androidx.lifecycle.ViewModel
import java.io.Serializable

/**
 * Táto trieda slúži pre riadenie jednotlivých stolov.
 *
 * Je potomkom triedy ViewModel a implementuje Serializable, aby ju bolo možné posielať ako objekt
 * pri prechode medzi fragmentami.
 *
 * @property zoznam Predstavuje zoznam stolov v systéme.
 */
class StolyViewModel : ViewModel(), Serializable {
    private val zoznam: MutableList<Stol> = mutableListOf()

    /**
     * Inicializačný blok, ktorý slúži inicializovanie daných stolov v systéme.
     */
    init {
        initStoly()
    }

    /**
     * Metóda, ktorá slúži na pridanie jednotlivých stolov do zoznamu.
     */
    private fun initStoly() {
        zoznam.apply {
            add(Stol(0.00, "STÔL 1"))
            add(Stol(0.00, "STÔL 2"))
            add(Stol(0.00, "STÔL 3"))
            add(Stol(0.00, "MALÝ GAUČ"))
            add(Stol(0.00, "VEĽKÝ GAUČ"))
            add(Stol(0.00, "TERASA 1"))
            add(Stol(0.00, "TERASA 2"))
            add(Stol(0.00, "TERASA 3"))
            add(Stol(0.00, "TERASA 4"))
            add(Stol(0.00, "TERASA 5"))
        }
    }

    /**
     * Metóda, ktorá slúži na získanie celého zoznamu stolov.
     *
     * @return zoznam stolov.
     */
    fun dajStoly(): MutableList<Stol> {
        return this.zoznam
    }

    /**
     * Metóda, ktorá slúži na získanie konkrétneho stolu zo zoznamu na základe jeho pozície v zozname.
     *
     * @return konrkrétny stôl alebo null.
    */
    fun dajKonkretnyStol(postion: Int): Stol? {
        return if (postion >= 0 && postion < this.zoznam.size) {
            this.zoznam[postion]
        } else {
            null
        }
    }
}