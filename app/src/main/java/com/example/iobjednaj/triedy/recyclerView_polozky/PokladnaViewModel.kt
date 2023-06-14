package com.example.iobjednaj.triedy.recyclerView_polozky

import androidx.lifecycle.ViewModel
import java.io.Serializable

/**
 * Táto trieda slúži pre správu finančných transakcií v pokladni.
 *
 * Je potomkom triedy ViewModel a implementuje Serializable, aby ju bolo možné posielať ako objekt
 * pri prechode medzi fragmentami.
 *
 * @property dennyVklad Predstavuje výšku zadaného denného vkladu.
 * @property transakcieKarty Predstavuje celkovú sumu zaplatenú kartami.
 * @property transakcieHotovost Predstavuje celkovú sumu zaplatenú v hotovosti.
 */
class PokladnaViewModel : ViewModel(), Serializable {
    private var dennyVklad: Double = 0.0
    private var transakcieKarty: Double = 0.0
    private var transakcieHotovost: Double = 0.0

    /**
     * Metóda, ktorá slúži na zvýšenie sumy v kartách.
     *
     * @param pSuma Suma, ktorá bola zaplatená kartou.
     */
    fun platbaKartou(pSuma: Double) {
        transakcieKarty += pSuma
    }

    /**
     * Metóda, ktorá slúži na zvýšenie sumy v hotovosti.
     *
     * @param pSuma Suma, ktorá bola zaplatená v hotovosti.
     */
    fun platbaHotovost(pSuma: Double) {
        transakcieHotovost += pSuma
    }

    /**
     * Metóda, ktorá slúži na nastavenie denného vkladu.
     *
     * @param pDennyVklad Výška zadavaného denného vkladu.
     */
    fun nastavDennyVklad(pDennyVklad: Double) {
        dennyVklad = pDennyVklad
    }

    /**
     * Metóda, ktorá slúži na získanie celkového množstva peňazí v pokladni.
     *
     * @return celkové množstvo v pokladni spolu.
     */
    fun kolkoVpokladni(): Double {
        return (dennyVklad) + transakcieKarty + transakcieHotovost
    }

    /**
     * Metóda, ktorá slúži pre zistenie, aká celková suma bola zaplatená v hotovosti.
     *
     * @return celková suma zaplatená v hotovosti.
     */
    fun kolkoVhotovosti(): Double {
        return transakcieHotovost
    }

    /**
     * Metóda, ktorá slúži pre zistenie, aká celková suma bola zaplatená kartou.
     *
     * @return celková suma zaplatená kartou.
     */
    fun kolkoKartkami(): Double {
        return transakcieKarty
    }

    /**
     * Metóda, ktorá slúži na zistenie denného vkladu.
     *
     * @return Vložený denný vklad
     */
    fun dajVklad(): Double {
        return dennyVklad
    }

    /**
     * Metóda, ktorá slúži na aktualizovanie pokladne, presnejšie pre anuláciu jednotlivých vlastností
     * tejto triedy.
     * Anulujú sa jednotlivé sumy.
     */
    fun aktualizujPokladnu() {
        this.dennyVklad = 0.0
        this.transakcieKarty = 0.0
        this.transakcieHotovost = 0.0
    }
}
