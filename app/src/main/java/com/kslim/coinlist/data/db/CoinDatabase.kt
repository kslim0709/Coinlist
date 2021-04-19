package com.kslim.coinlist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kslim.coinlist.data.model.CoinExplain
import com.kslim.coinlist.data.model.CoinExplainDao
import com.kslim.coinlist.data.model.CoinInfoDao
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.utils.Constants

@Database(entities = [CoinInformation::class, CoinExplain::class], version = 1)
abstract class CoinDatabase : RoomDatabase() {

    abstract fun getCoinInfoDatabase(): CoinInfoDao
    abstract fun getCoinExplainDatabase(): CoinExplainDao

    companion object {
        private var coinDatabase: CoinDatabase? = null

        fun getInstance(context: Context): CoinDatabase =
            (coinDatabase ?: synchronized(CoinDatabase::class) {
                coinDatabase ?: Room.databaseBuilder(
                    context.applicationContext,
                    CoinDatabase::class.java,
                    Constants.DB_NAME
                ).build().also {
                    coinDatabase = it
                }
            })
    }

}