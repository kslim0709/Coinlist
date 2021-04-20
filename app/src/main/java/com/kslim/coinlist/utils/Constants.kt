package com.kslim.coinlist.utils

object Constants {
    const val TAG_MAIN = "MainActivity"
    const val TAG_DETAILS = "DetailsActivity"

    const val INTENT_KEY_COIN_TICKER = "coin_ticker"

    const val FG_COIN_LIST: Int = 0
    const val FG_COIN_FAVORITE: Int = 1
    const val FG_COIN_EXPLAIN: Int = 2

    const val BTC_TICKER = "KRW-BTC"
    const val BTC = "btc"

    const val EVEN = "EVEN"
    const val RISE = "RISE"
    const val FALL = "FALL"

    const val REPEAT_TIME: Long = 1
    const val CHART_BONG: Int = 36

    const val DB_NAME = "coin.db"
}


object API {
    // Upbit API
    const val UPBIT_BASE_URL: String = "https://api.upbit.com/"
    const val UPBIT_ACCESS_KEY = "1xu4NUk5LTP16UrnwmqJIe9O0xuvHsCzc3P4lgqx"
    const val UPBIT_SECRET_KEY = "1E29CclgrReOFxDTM12XS5T7imgxxqZiNfPMSuGR"

    const val REQ_COIN_LIST = "v1/market/all"
    const val REQ_COIN_TICKER_LIST = "v1/ticker"
    const val REQ_COIN_CANDLES_DAYS = "v1/candles/days"
    const val REQ_COIN_CANDLES_WEEKS = "v1/candles/weeks"
    const val REQ_COIN_CANDLES_MONTH = "v1/candles/months"

    // ICON Base Url
    const val ICON_BASE_URL: String = "https://cryptoicon-api.vercel.app/api/icon/"

    //// Coin Market Cap API
    const val COIN_MARKET_CAP_BASE_URL: String = "https://pro-api.coinmarketcap.com/"
    const val COIN_MARKET_CAP_API_KEY: String = "f012d2c2-ea68-4302-b6fa-990be8de2d43"

    const val REQ_COIN_META_DATA = "v1/cryptocurrency/info"


}