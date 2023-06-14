package com.example.iobjednaj.triedy.interfaces;

/**
 * Interface, ktorý slúži na interakciu, keď užívateľ klikne na príslušný stôl v recyclerView, na
 * ktorý chce vytvoriť objednávku.
 */
public interface IRecyclerViewInterface {

    /**
     * Operácia, ktorá slúži na získanie konkrétneho stola, na ktorý sa klikne.
     *
     * @param position Pozícia daného stola v zozname stolov.
     */
    void onItemClick(int position);
}
