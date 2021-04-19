package com.kslim.coinlist.data.model

import com.kslim.coinlist.utils.API
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UpbitDao {
    // https://api.upbit.com/v1/market/all
    @GET(API.REQ_COIN_LIST)
    fun requestAllCoinList(): Observable<List<Coin>>

    // https://api.upbit.com/v1/ticker?markets = ""
    @GET(API.REQ_COIN_TICKER_LIST)
    fun requestCoinTickerList(@Query("markets") markets: String): Observable<List<CoinTicker>>
}