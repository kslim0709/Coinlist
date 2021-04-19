package com.kslim.coinlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kslim.coinlist.data.DataManager
import com.kslim.coinlist.data.model.CoinExplain
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.data.model.CoinTicker
import com.kslim.coinlist.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SharedViewModel : ViewModel() {

    private var mDisposable: Disposable? = null
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    private val dataManager = DataManager.getInstance()

    private val _explainCoinList = MutableLiveData<List<CoinExplain>>()
    val explainCoinList
        get() = _explainCoinList

    private val _favoriteCoinTickerList = MutableLiveData<List<CoinInformation>>()
    val favoriteCoinTickerList
        get() = _favoriteCoinTickerList

    private val _coinTickerList = MutableLiveData<List<CoinTicker>>()
    val coinTickerList: LiveData<List<CoinTicker>>
        get() = _coinTickerList

    private val _editText = MutableLiveData<String>()
    val editText: LiveData<String>
        get() = _editText

    fun setEditText(e: String) {
        _editText.value = e
    }

    fun requestAllCoinTicker(ticker: String) {
        mDisposable = dataManager.getCoinPickerList(ticker)
            .repeatWhen { complete -> complete.delay(1, TimeUnit.MINUTES) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i(Constants.TAG_MAIN, "requestBitCoinTicker: ${it}")
                _coinTickerList.postValue(it)
            }, {
                Log.e(Constants.TAG_MAIN, "requestBitCoinTicker Exception: ${it.message}")
            })
        mCompositeDisposable.add(mDisposable!!)

//        mCompositeDisposable.add(
//            dataManager.getCoinPickerList(ticker)
//                .repeatWhen { complete -> complete.delay(1, TimeUnit.MINUTES) }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Log.i(Constants.TAG_MAIN, "requestBitCoinTicker: ${it}")
//                    _coinTickerList.postValue(it)
//                }, {
//                    Log.e(Constants.TAG_MAIN, "requestBitCoinTicker Exception: ${it.message}")
//                })
//        )
    }

    fun stopRequestAppCoinTicker() {
        if (mDisposable != null) {
            try {
                mCompositeDisposable.remove(mDisposable!!)
                mDisposable!!.dispose()

            } catch (e: NullPointerException) {
                e.printStackTrace()
            } finally {
                mDisposable = null
            }
        }
    }

    fun getAllFavoriteCoinList() {
        mCompositeDisposable.add(
            dataManager.getAllFavoriteCoinList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(Constants.TAG_MAIN, "getAllFavoriteCoinList: $it")
                    _favoriteCoinTickerList.postValue(it)
                }, {
                    Log.e(Constants.TAG_MAIN, "getAllFavoriteCoinList Exception: ${it.message}")
                })
        )
    }

    fun requestCoinMetaData(markets: String) {
        mCompositeDisposable.add(
            dataManager.requestCoinMetaData(markets)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ coinMetadata ->
                    Log.i(Constants.TAG_MAIN, "requestCoinMetaData: $coinMetadata")
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
                }, {
                    Log.e(Constants.TAG_MAIN, "requestCoinMetaData Exception: ${it.message}")
                })
        )
    }

    fun getDBAllCoinExplain() {
        mCompositeDisposable.add(
            dataManager.getDBAllCoinExplain()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(Constants.TAG_MAIN, "getDBAllCoinExplain: $it")
                    _explainCoinList.postValue(it)
                }, {
                    Log.e(Constants.TAG_MAIN, "getDBAllCoinExplain Exception: ${it.message}")
                })
        )
    }


    override fun onCleared() {
        mCompositeDisposable.dispose()
        super.onCleared()
    }


}