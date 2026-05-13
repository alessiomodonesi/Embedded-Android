package com.example.android.roomwordssample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Classe astratta del database Room.
 * Definisce le entità (Word) e fornisce l'accesso ai DAO.
 */
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    // Riferimento astratto al DAO
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        // Popola il database quando viene creato per la prima volta
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val wordDao = database.wordDao()

                    // Pulisce il database ad ogni avvio (opzionale)
                    wordDao.deleteAll()

                    // Inserisce alcune parole iniziali
                    wordDao.insert(Word("Ciao"))
                    wordDao.insert(Word("Mondo"))
                    wordDao.insert(Word("Room"))
                }
            }
        }
    }

    companion object {
        // Il pattern Singleton previene l'apertura di più istanze del database contemporaneamente.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // Se INSTANCE non è null, ritorna l'istanza. Altrimenti, crea il database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                // Aggiunge il callback per popolare il database alla sua creazione
                .addCallback(WordDatabaseCallback(scope))
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}