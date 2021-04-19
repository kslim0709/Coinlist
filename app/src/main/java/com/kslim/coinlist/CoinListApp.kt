package com.kslim.coinlist

import android.app.Application
import android.content.Context

class CoinListApp : Application() {

    companion object {
        lateinit var instance: CoinListApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getContext(): Context = applicationContext

}