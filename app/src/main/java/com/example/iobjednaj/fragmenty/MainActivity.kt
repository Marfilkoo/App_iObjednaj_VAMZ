package com.example.iobjednaj.fragmenty


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iobjednaj.R

/**
 * Hlavná aktivita (Main Activity) aplikácie iObjednaj.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Metóda, ktorá sa volá, keď sa spustí aplikácia, respektíve, keď sa začína aktivita.
     *
     * @param savedInstanceState Uložený stav inštancie.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}