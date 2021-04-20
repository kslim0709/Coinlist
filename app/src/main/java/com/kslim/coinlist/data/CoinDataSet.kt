package com.kslim.coinlist.data

import com.kslim.coinlist.data.model.Coin
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.data.model.CoinTicker

class CoinDataSet private constructor() {
    private lateinit var coinList: ArrayList<Coin>
    private lateinit var coinTickerList: ArrayList<CoinTicker>

    companion object {
        @Volatile
        private var coinDataSet: CoinDataSet? = null

        fun getInstance(): CoinDataSet = coinDataSet ?: synchronized(this) {
            coinDataSet ?: CoinDataSet().also { coinDataSet = it }
        }
    }

    fun setCoinList(coinList: ArrayList<Coin>) {
        this.coinList = coinList
    }

    fun setCoinTickerList(coinTickerList: ArrayList<CoinTicker>) {
        this.coinTickerList = coinTickerList
    }

    fun getCoinList(): ArrayList<Coin> {
        return coinList
    }

    fun getCoinTickerList(): ArrayList<CoinTicker> {
        return coinTickerList
    }

    fun getCoinInformationList(): ArrayList<CoinInformation> {
        val coinInfoList = arrayListOf<CoinInformation>()
        for (coin in getCoinList()) {
            for (coinTicker in getCoinTickerList()) {
                if (coin.market.equals(coinTicker.market, true)) {
                    coinInfoList.add(
                        CoinInformation(
                            coin.market,
                            coin.koreanName,
                            coin.englishName,
                            coinTicker.change,
                            coinTicker.tradePrice,
                            coinTicker.accTradePrice24h
                        )
                    )
                    break
                }
            }
        }
        return coinInfoList
    }
}