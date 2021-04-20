package com.kslim.coinlist.data.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kslim.coinlist.utils.API
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object RetrofitClient {

    private lateinit var upbitApiClient: Retrofit
    private lateinit var coinMarketCapApiClient: Retrofit

    fun getUpbitApiClient(baseUrl: String): Retrofit {

        upbitApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createHttpClient(UpbitHttpInterceptor()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return upbitApiClient
    }

    fun getCoinMarketCapApiClient(baseUrl: String): Retrofit {

        coinMarketCapApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createHttpClient(CoinMarketCapHttpInterceptor()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return coinMarketCapApiClient
    }


    private fun createHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

    class UpbitHttpInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val algorithm = Algorithm.HMAC256(API.UPBIT_SECRET_KEY)
            val jwtToken = JWT.create()
                .withClaim("access_key", API.UPBIT_ACCESS_KEY)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm)
            val authenticationToken = "Bearer $jwtToken"

            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", authenticationToken)
                .build()
            return chain.proceed(newRequest)
        }
    }

    class CoinMarketCapHttpInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", API.COIN_MARKET_CAP_API_KEY)
                .build()
            return chain.proceed(newRequest)
        }
    }
}