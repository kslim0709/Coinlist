package com.kslim.coinlist.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CoinInfoDao {

    @Query("SELECT * FROM coin_info")
    fun getAllFavoriteCoin(): Observable<List<CoinInformation>>

    @Query("SELECT COUNT(*) FROM coin_info WHERE market =:market")
    fun checkIsFavoriteCoin(market: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteCoin(coinInformation: CoinInformation): Completable

    @Query("DELETE FROM coin_info WHERE market =:market")
    fun deleteFavoriteCoin(market: String): Completable
}