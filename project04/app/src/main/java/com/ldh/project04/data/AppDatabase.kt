package com.ldh.project04.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ldh.project04.data.dao.HistoryDao
import com.ldh.project04.data.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}