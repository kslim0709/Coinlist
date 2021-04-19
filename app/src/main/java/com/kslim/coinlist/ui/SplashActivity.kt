package com.kslim.coinlist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kslim.coinlist.R
import com.kslim.coinlist.data.CoinDataSet
import com.kslim.coinlist.data.DataManager
import com.kslim.coinlist.data.model.Coin
import com.kslim.coinlist.data.model.CoinExplain
import com.kslim.coinlist.data.model.CoinTicker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashActivity : AppCompatActivity() {
    private val TAG: String = "SplashActivity"

    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initCoinData()

    }

    private fun initCoinData() {
        mCompositeDisposable.add(
            DataManager.getInstance().getAllCoinList()
                .flatMap {
                    Observable.fromIterable(it).filter { coin ->
                        coin.market.contains("KRW-")
                    }.toList().toObservable()
                }.doOnNext { coinList ->
                    CoinDataSet.getInstance()
                        .setCoinList(coinList = coinList as ArrayList<Coin>)
                }.flatMap { coinList ->
                    val markets =
                        coinList.asSequence().map(Coin::market).joinToString(separator = ",")
                    DataManager.getInstance().getCoinPickerList(markets)
                }.doOnNext { coinTickerList ->
                    CoinDataSet.getInstance()
                        .setCoinTickerList(coinTickerList as ArrayList<CoinTicker>)
                }.flatMap { coinList ->
                    val markets =
                        coinList.subList(0, 30)
                            .map { it.market.substring(it.market.lastIndexOf("-") + 1) }
                            .toList()
                            .joinToString(separator = ",")
                    DataManager.getInstance().requestCoinMetaData(markets)
                }.doOnNext { coinMetadata ->
                    if (coinMetadata.status.errorCode == 0) {
                        val coinExplain = ArrayList<CoinExplain>()
                        coinMetadata.data.forEach {
                            coinExplain.add(
                                CoinExplain(
                                    it.key,
                                    it.value.logo,
                                    it.value.id,
                                    it.value.name,
                                    it.value.symbol,
                                    it.value.slug,
                                    it.value.description
                                )
                            )
                        }
                        DataManager.getInstance().insertAllCoinExplain(coinExplain)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coinMetadata ->
                    Log.v(TAG, "receive complete: ${coinMetadata.toString()}")

                    //startMainActivity()
                }, {
                    Log.e(TAG, "receive Exception ${it.message}")
                }, {
                    Log.v(TAG, "onComplete")
                    startMainActivity()
                })
        )
    }

    private fun startMainActivity() {
        val intent = Intent(
            this@SplashActivity,
            MainActivity::class.java
        ).also { it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        mCompositeDisposable.dispose()
        super.onDestroy()
    }

}