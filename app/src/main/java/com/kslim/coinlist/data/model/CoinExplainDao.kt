package com.kslim.coinlist.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single


@Dao
interface CoinExplainDao {

    @Query("SELECT * FROM coin_explain")
    fun getAllCoinExplain(): Single<List<CoinExplain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCoinExplain(coinExplain: List<CoinExplain>)
}