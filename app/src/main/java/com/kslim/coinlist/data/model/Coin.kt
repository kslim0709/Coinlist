package com.kslim.coinlist.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Coin(
    @SerializedName("market") val market: String,
    @SerializedName("korean_name") val koreanName: String,
    @SerializedName("english_name") val englishName: String,
)

data class CoinTicker(
    @SerializedName("market")
    val market: String,
    @SerializedName("trade_date")
    val tradeDate: String,
    @SerializedName("trade_time")
    val tradeTime: String,
    @SerializedName("trade_date_kst")
    val tradeDateKst: String,
    @SerializedName("trade_time_kst")
    val tradeTimeKst: String,
    @SerializedName("trade_timestamp")
    val tradeTimestamp: Double,
    @SerializedName("opening_price")
    val openingPrice: Double,
    @SerializedName("high_price")
    val highPrice: Double,
    @SerializedName("low_price")
    val lowPrice: Double,
    @SerializedName("trade_price")
    val tradePrice: Double,
    @SerializedName("prev_closing_price")
    val prevClosingPrice: Double,
    @SerializedName("change")
    val change: String,
    @SerializedName("change_price")
    val changePrice: Double,
    @SerializedName("change_rate")
    val changeRate: Double,
    @SerializedName("signed_change_price")
    val signedChangePrice: Double,
    @SerializedName("signed_change_rate")
    val signedChangeRate: Double,
    @SerializedName("trade_volume")
    val tradeVolume: Double,
    @SerializedName("acc_trade_price")
    val accTradePrice: Double,
    @SerializedName("acc_trade_price_24h")
    val accTradePrice24h: Double,
    @SerializedName("acc_trade_volume")
    val accTradeVolume: Double,
    @SerializedName("acc_trade_volume_24h")
    val accTradeVolume24h: Double,
    @SerializedName("highest_52_week_price")
    val highest52WeekPrice: Double,
    @SerializedName("highest_52_week_date")
    val highest52WeekDate: String,
    @SerializedName("lowest_52_week_price")
    val lowest52WeekPrice: Double,
    @SerializedName("lowest_52_week_date")
    val lowest52WeekDate: String,
    @SerializedName("timestamp")
    val timestamp: Long
)

@Entity(tableName = "coin_info")
@Parcelize
data class CoinInformation(
    @PrimaryKey val market: String,
    val koreanName: String,
    val englishName: String,
    val change: String,
    val tradePrice: Double,
    val accTradePrice24h: Double
) : Parcelable

data class CoinMetadata(
    @SerializedName("status") val status: Status,
    @SerializedName("data") val data: HashMap<String, Meta>
)

data class Status(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
    @SerializedName("elapsed") val elapsed: Int,
    @SerializedName("credit_count") val creditCount: Int
)

data class Meta(
    @SerializedName("logo") val logo: String,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String
)

@Entity(tableName = "coin_explain")
data class CoinExplain(
    @PrimaryKey val uid: String,
    val logo: String,
    val id: Long,
    val name: String,
    val symbol: String,
    val slug: String,
    val description: String
)