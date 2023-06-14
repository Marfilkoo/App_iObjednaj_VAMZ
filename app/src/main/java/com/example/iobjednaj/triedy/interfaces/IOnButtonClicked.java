package com.example.iobjednaj.triedy.interfaces;

/**
 * Interface, ktorý zabezpečuje interakciu, keď užívateľ klikne na tlačidlo + alebo -
 * príslušnej položky pri objednávaní.
 */
public interface IOnButtonClicked {
    /**
     * Operácia, ktorá slúži pre operáciu pridania položky do objednávky.
     */
    void onPridajClicked();

    /**
     * Operácia, ktorá slúži pre operáciu odobranie položky do objednávky.
     */
    void onUberClicked();
}
