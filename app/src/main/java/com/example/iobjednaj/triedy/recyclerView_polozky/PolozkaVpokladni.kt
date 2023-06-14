package com.example.iobjednaj.triedy.recyclerView_polozky

import java.io.Serializable

/**
 * Táto trieda rerezentuje jednu položku v pokladni, ktorá je charakteristicá svojim názvom, cenou
 * a cenou.
 *
 * Táto trieda implementuje interface Serializable, aby ju bolo požné posielať ako objekt medzi fragmentami.
 *
 * @param nazovPolozky Názov danej položky v pokladni.
 * @param cenaPolozky Cena danej položky v pokladni.
 * @property pocetObjednanych Objednané množstvo danej položky v pokladni.
 */
class PolozkaVpokladni(private val nazovPolozky: String, private val cenaPolozky: Double) : Serializable {

    private var pocetObjednanych = 0

    /**
     * Metóda, ktorá slúži na získanie názvu danej položky.
     *
     * @return názov položky.
     */
    fun dajNazovPolozky(): String {
        return this.nazovPolozky
    }

    /**
     * Metóda, ktorá slúži na získanie ceny danej položky.
     *
     * @return cena položky.
     */
    fun dajCenuPolozky(): Double {
        return cenaPolozky
    }

    /**
     * Metóda, ktorá slúži na zvýšenie objednaného množstva danej položky.
     */
    fun zvysObjednanie() {
        pocetObjednanych++
    }

    /**
     * Metóda, ktorá slúži na zníženie objednaného množstva danej položky.
     * Nemožno sa dostať do záporných hodnôt.
     */
    fun znizObjednanie() {
        if (pocetObjednanych > 0) {
            pocetObjednanych--
        }
    }

    /**
     * Metóda, ktorá slúži na získanie objednaného množstva príslušnej položky.
     *
     * @return počet objednaných.
     */
    fun dajPocetObjednanych(): Int {
        return pocetObjednanych
    }

    /**
     * Metóda, ktorá slúži na anuláciu objednaného množstva danej položky.
     */
    fun anulujPocetObjednanych() {
        pocetObjednanych = 0
    }

    /**
     * Metóda, ktorá slúži na zmenu objednaného množstva na konkrétnu hodnotu.
     *
     * @param mnozstvo nová hodnota objednaného množstva danej položky, ktorá nesmie byť záporná.
     */
    fun zmenMnozstvo(mnozstvo: Int) {
        if (mnozstvo >= 0) {
            pocetObjednanych = mnozstvo
        }
    }
}
