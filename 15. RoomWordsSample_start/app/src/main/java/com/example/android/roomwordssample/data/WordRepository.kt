package com.example.android.roomwordssample.data

import androidx.lifecycle.LiveData

/**
 * Repository che astrae l'accesso ai dati (dal DAO).
 * Gestisce le operazioni sui dati fornendo un'API pulita al resto dell'app.
 */
class WordRepository(private val wordDao: WordDao) {

    // Ottiene la LiveData contenente tutte le parole ordinate alfabeticamente dal DAO.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    // Metodo suspend per inserire una parola nel database in modo asincrono.
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}