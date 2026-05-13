package com.example.android.roomwordssample.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) per l'entità Word.
 * Fornisce i metodi per interagire con la tabella "word_table" nel database.
 */
@Dao
interface WordDao {

    // Ottiene tutte le parole ordinate alfabeticamente.
    // Utilizziamo LiveData per osservare i cambiamenti nel database in tempo reale.
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    // Inserisce una nuova parola nel database. Se la parola esiste già, la ignora.
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(word: Word)

    // Elimina tutte le parole dal database.
    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}