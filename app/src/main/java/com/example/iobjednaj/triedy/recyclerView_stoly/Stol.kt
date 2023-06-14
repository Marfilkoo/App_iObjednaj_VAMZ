package com.example.iobjednaj.triedy.recyclerView_stoly

import com.example.iobjednaj.triedy.recyclerView_polozky.PolozkaVpokladni
import java.io.Serializable

/**
 * Trieda, ktorá reprezentuje daný stol v zozname stolov s danými položkami, ktoré si možno objednať
 * na príslušný stôl.
 *
 * @param sumaUctu Predstavuje sumu účtu na danom stole.
 * @param nazovStola Predstavuje názov daného stola v zozname.
 * @property polozkyVpokladni Predstavuje položky v pokladni, ktoré možno pripísať na daný stôl.
 */
class Stol(private var sumaUctu: Double, private val nazovStola: String) : Serializable {

    private val polozkyVpokladni: ArrayList<PolozkaVpokladni> = ArrayList()

    /**
     * Inicializačný blok, ktorý slúži na inicializovanie položiek v pokladni.
     */
    init {
        initPolozky()
    }

    /**
     * Metóda, ktorá slúži na inicializovanie jednotlivých položiek.
     */
    private fun initPolozky() {
        polozkyVpokladni.apply {
            add(PolozkaVpokladni("Espresso", 1.80))
            add(PolozkaVpokladni("Lungo", 1.80))
            add(PolozkaVpokladni("Cappuccino", 2.50))
            add(PolozkaVpokladni("Cappuccino AROMA", 3.00))
            add(PolozkaVpokladni("Latte", 2.80))
            add(PolozkaVpokladni("Latte AROMA", 3.30))
            add(PolozkaVpokladni("Turecká káva", 1.80))
            add(PolozkaVpokladni("Smotana do kavy 1cl", 0.95))
            add(PolozkaVpokladni("Čaj LEROS", 2.30))
            add(PolozkaVpokladni("Med", 0.50))
            add(PolozkaVpokladni("Citrón", 0.70))
            add(PolozkaVpokladni("Coca-Cola ORIGINAL", 1.90))
            add(PolozkaVpokladni("Fanta ORANGE", 2.10))
            add(PolozkaVpokladni("Sprite", 2.10))
            add(PolozkaVpokladni("FuzeTea ALOE", 1.70))
            add(PolozkaVpokladni("FuzeTea BROSKYŇA", 1.70))
            add(PolozkaVpokladni("Targa LEMON", 2.00))
            add(PolozkaVpokladni("Targa ORANGE", 2.00))
            add(PolozkaVpokladni("Targa GRAPEFRUIT", 2.00))
            add(PolozkaVpokladni("M.O.M Glittery", 5.80))
            add(PolozkaVpokladni("Mojito", 4.20))
            add(PolozkaVpokladni("Mojito JAHODA", 5.10))
            add(PolozkaVpokladni("Mojito KIWI", 5.10))
            add(PolozkaVpokladni("Targa Fizz", 5.70))
            add(PolozkaVpokladni("Blue Lady", 4.80))
            add(PolozkaVpokladni("Kiwi-Rose GIN", 6.80))
        }
    }

    /**
     * Metóda, ktorá slúži na prepočitanie sumy objednaného množstva položiek.
     */
    fun prepocitajSumuStola() {
        var prepocitanaSumaUctu = 0.0
        for (polozka in polozkyVpokladni) {
            prepocitanaSumaUctu += polozka.dajCenuPolozky() * polozka.dajPocetObjednanych()
        }
        sumaUctu = prepocitanaSumaUctu
    }

    /**
     * Metóda, ktorá slúži na upravenie objednaného množstva každej jednej položky.
     *
     * @param upravenyStol Stol, ktorý má aktualizovaný zoznam položiek, na ktoré sa má zmeniť hodnota.
     */
    fun upravObjednanePolozky(upravenyStol: Stol) {
        if (polozkyVpokladni.size == upravenyStol.polozkyVpokladni.size) {
            for(i in 0 until polozkyVpokladni.size) {
                if (polozkyVpokladni[i].dajNazovPolozky() == upravenyStol.polozkyVpokladni[i].dajNazovPolozky()) {
                    polozkyVpokladni[i].zmenMnozstvo(upravenyStol.polozkyVpokladni[i].dajPocetObjednanych())
                }
            }
        }
    }

    /**
     * Metóda, ktorá slúži na získanie celého zoznamu položiek na danom stole.
     *
     * @return všetky položky na danom stole.
     */
    fun dajPolozky(): ArrayList<PolozkaVpokladni> {
        return polozkyVpokladni
    }

    /**
     * Metóda, ktorá vráti sumu objednávky daného stola.
     *
     * @return suma objednávky daného stola.
     */
    fun dajSumuUctu(): Double {
        return sumaUctu
    }

    /**
     * Metóda, ktorá vráti názov daného stola.
     *
     * @return názov daného stola.
     */
    fun dajNazovStola(): String {
        return nazovStola
    }

    /**
     * Metóda, ktorá anuluje objednané množstvo jednotlivých položiek na danom stole.
     */
    fun anulujSumu() {
        for (polozka in polozkyVpokladni) {
            polozka.anulujPocetObjednanych()
        }
        prepocitajSumuStola()
    }
}