/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class FlowerAdapter(private val flowerList: Array<String>) :
    RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder>() {

    val mTAG : String = this.javaClass.simpleName

    /**
     * Rappresenta l'involucro (container) per la view di un singolo elemento della lista.
     * * Il compito principale di questa classe è mantenere i riferimenti (cache) alle
     * sotto-view del layout (es. TextView, ImageView) per evitare costose chiamate ripetute
     * a [View.findViewById] durante lo scorrimento della RecyclerView.
     *
     * @param itemView La view radice (root view) del layout dell'elemento (flower_item.xml).
     */
    class FlowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Il riferimento alla TextView viene "cercato" e salvato una sola volta
        // al momento della creazione del ViewHolder.
        private val flowerTextView: TextView = itemView.findViewById(R.id.flower_text)

        /**
         * Associa i dati specifici di un fiore agli elementi visivi (UI).
         * * Centralizzando la logica di binding qui dentro, manteniamo l'Adapter pulito
         * e separiamo le responsabilità.
         *
         * @param word La stringa di testo da mostrare (es. l'indice e il nome del fiore).
         */
        fun bind(word: String) {
            // holder.bind("$position - ${flowerList[position]}")
            flowerTextView.text = word
        }
    }

    /**
     * Invocato quando la RecyclerView necessita di un nuovo [FlowerViewHolder].
     *
     * Questo metodo si occupa esclusivamente di "gonfiare" (inflate) il layout XML
     * per creare la struttura visiva dell'elemento, senza popolarne i dati.
     * Grazie al meccanismo di riciclo, viene chiamato solo un numero limitato di volte.
     *
     * @param parent Il ViewGroup (la RecyclerView stessa) a cui la view sarà associata.
     * @param viewType L'identificatore del tipo di view (utile se la lista ha layout multipli).
     * @return Una nuova istanza di FlowerViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flower_item, parent, false)

        // ViewHolders creati solo una volta
        Log.v(mTAG, "creato un nuovo ViewHolder")
        return FlowerViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return flowerList.size
    }

    /**
     * Invocato dalla RecyclerView per visualizzare i dati in una posizione specifica.
     *
     * Questo metodo è responsabile dell'aggiornamento dell'interfaccia utente del
     * [FlowerViewHolder] (sia esso nuovo o riciclato) con i dati corrispondenti
     * all'elemento in [position].
     *
     * @param holder Il ViewHolder che deve essere aggiornato con i nuovi contenuti.
     * @param position L'indice dell'elemento all'interno della [flowerList].
     */
    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        // Popola la UI combinando l'indice e il nome del fiore
        holder.bind("$position - ${flowerList[position]}")
        // update ogni volta che mostro di nuovo il singolo holder
        Log.v(mTAG, "ViewHolder riempito di dati")
    }
}
