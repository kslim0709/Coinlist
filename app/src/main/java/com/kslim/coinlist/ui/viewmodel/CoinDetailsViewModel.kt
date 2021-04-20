package com.kslim.coinlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kslim.coinlist.data.DataManager
import com.kslim.coinlist.data.model.CoinCandle
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.data.model.CoinTicker
import com.kslim.coinlist.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinDetailsViewModel : ViewModel() {

    private var dataManager: DataManager = DataManager.getInstance()
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _coinDayCandles = MutableLiveData<List<CoinCandle>>()
    val coinDayCandles: LiveData<List<CoinCandle>>
        get() = _coinDayCandles

    private val _coinTickerList = MutableLiveData<List<CoinTicker>>()
    val coinTickerList: LiveData<List<CoinTicker>>
        get() = _coinTickerList

    private val _isCheck = MutableLiveData<Int>()
    val isCheck: LiveData<Int>
        get() = _isCheck

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess


    fun requestAllCoinTicker(ticker: String) {
        mCompositeDisposable.add(dataManager.getCoinPickerList(ticker)
            .repeatWhen { complete -> complete.delay(Constants.REPEAT_TIME, TimeUnit.MINUTES) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Log.i(Constants.TAG_DETAILS, "requestAllCoinTicker: $it")
                _coinTickerList.postValue(it)
            }, {
                Log.e(Constants.TAG_DETAILS, "requestAllCoinTicker Exception: ${it.message}")
            })
        )
    }

    fun requestCoinDayCandlesData(market: String, count: Int) {
        mCompositeDisposable.add(
            dataManager.getCoinDayCandlesData(market, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.i(Constants.TAG_DETAILS, "requestCoinDayCandlesData: $it")
                    _coinDayCandles.postValue(it)
                }, {
                    Log.e(
                        Constants.TAG_DETAILS,
                        "requestCoinDayCandlesData Exception: ${it.message}"
                    )
                })
        )
    }

    fun requestCoinWeeksCandlesData(market: String, count: Int) {
        mCompositeDisposable.add(
            dataManager.getCoinWeeksCandlesData(market, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.i(Constants.TAG_DETAILS, "requestCoinDayCandlesData: $it")
                    _coinDayCandles.postValue(it)
                }, {
                    Log.e(
                        Constants.TAG_DETAILS,
                        "requestCoinDayCandlesData Exception: ${it.message}"
                    )
                })
        )
    }

    fun requestCoinMonthCandlesData(market: String, count: Int) {
        mCompositeDisposable.add(
            dataManager.getCoinMonthCandlesData(market, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //Log.i(Constants.TAG_DETAILS, "requestCoinDayCandlesData: $it")
                    _coinDayCandles.postValue(it)
                }, {
                    Log.e(
                        Constants.TAG_DETAILS,
                        "requestCoinDayCandlesData Exception: ${it.message}"
                    )
                })
        )
    }

    fun insertFavoriteCoin(information: CoinInformation) {
        mCompositeDisposable.add(
            dataManager.insertFavoriteCoin(information)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _isSuccess.value = true
                }, {
                    _isSuccess.value = false
                })
        )
    }

    fun deleteFavoriteCoin(market: String) {
        mCompositeDisposable.add(
            dataManager.deleteFavoriteCoin(market)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _isSuccess.value = false
                }, {
                    _isSuccess.value = true
                })
        )
    }

    fun checkIsFavoriteCoin(market: String) {
        mCompositeDisposable.add(
            dataManager.checkIsFavoriteCoin(market)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _isCheck.value = it
                }, {
                    Log.e(Constants.TAG_DETAILS, "checkIsFavoriteCoin Exception: ${it.message}")
                })
        )
    }

    override fun onCleared() {
        mCompositeDisposable.dispose()
        super.onCleared()
    }
}