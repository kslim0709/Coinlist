package com.kslim.coinlist.data.model

import com.kslim.coinlist.utils.API
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapDao {
    // https://pro-api.coinmarketcap.com/v1/cryptocurrency/info
    @GET(API.REQ_COIN_META_DATA)
    fun requestCoinMetaData(@Query("symbol") symbol: String): Observable<CoinMetadata>
}