package com.kslim.coinlist.data

import com.kslim.coinlist.CoinListApp
import com.kslim.coinlist.data.api.RetrofitClient
import com.kslim.coinlist.data.db.CoinDatabase
import com.kslim.coinlist.data.model.*
import com.kslim.coinlist.utils.API
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class DataManager private constructor() {

    companion object {
        @Volatile
        private var dataManager: DataManager? = null

        fun getInstance(): DataManager = dataManager ?: synchronized(this) {
            dataManager ?: DataManager().also { dataManager = it }
        }
    }


    // Upbit
    private val upbitDao: UpbitDao by lazy {
        RetrofitClient.getUpbitApiClient(API.UPBIT_BASE_URL).create(UpbitDao::class.java)
    }

    //CoinMarketCap
    private val coinMarketCapDao: CoinMarketCapDao by lazy {
        RetrofitClient.getCoinMarketCapApiClient(API.COIN_MARKET_CAP_BASE_URL)
            .create(CoinMarketCapDao::class.java)
    }

    // CoinInfo Database
    private val coinInfoDao: CoinInfoDao by lazy {
        CoinDatabase.getInstance(CoinListApp.instance.getContext()).getCoinInfoDatabase()
    }

    // Coin Explain Database
    private val coinExplainDao: CoinExplainDao by lazy {
        CoinDatabase.getInstance(CoinListApp.instance.getContext()).getCoinExplainDatabase()
    }

    // Upbit API Call
    fun getAllCoinList(): Observable<List<Coin>> {
        return upbitDao.requestAllCoinList()
    }

    fun getCoinPickerList(markets: String): Observable<List<CoinTicker>> {
        return upbitDao.requestCoinTickerList(markets)
    }

    fun getCoinDayCandlesData(market: String, count: Int): Observable<List<CoinCandle>> {
        return upbitDao.requestCoinDayCandlesData(market, count)
    }

    fun getCoinWeeksCandlesData(market: String, count: Int): Observable<List<CoinCandle>> {
        return upbitDao.requestCoinWeeksCandlesData(market, count)
    }

    fun getCoinMonthCandlesData(market: String, count: Int): Observable<List<CoinCandle>> {
        return upbitDao.requestCoinMonthCandlesData(market, count)
    }

    // CoinMarketCap Api Call
    fun requestCoinMetaData(symbol: String): Observable<CoinMetadata> {
        return coinMarketCapDao.requestCoinMetaData(symbol)
    }

    // Data Coin Explain Call
    fun insertAllCoinExplain(coinExplain: List<CoinExplain>) {
        return coinExplainDao.insertAllCoinExplain(coinExplain)
    }

    fun getDBAllCoinExplain(): Single<List<CoinExplain>> {
        return coinExplainDao.getAllCoinExplain()
    }

    // Database CoinInfo Call
    fun getAllFavoriteCoinList(): Observable<List<CoinInformation>> {
        return coinInfoDao.getAllFavoriteCoin()
    }

    fun checkIsFavoriteCoin(market: String): Single<Int> {
        return coinInfoDao.checkIsFavoriteCoin(market)
    }

    fun insertFavoriteCoin(information: CoinInformation): Completable {
        return coinInfoDao.insertFavoriteCoin(information)
    }

    fun deleteFavoriteCoin(market: String): Completable {
        return coinInfoDao.deleteFavoriteCoin(market)
    }


}

