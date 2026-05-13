package com.example.android.roomwordssample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entità che rappresenta una parola nel database.
 * Ogni istanza di questa classe rappresenta una riga nella tabella "word_table".
 */
@Entity(tableName = "word_table")
data class Word(
    // Definiamo la colonna "word" come chiave primaria.
    @PrimaryKey
    @ColumnInfo(name = "word")
    val word: String
)