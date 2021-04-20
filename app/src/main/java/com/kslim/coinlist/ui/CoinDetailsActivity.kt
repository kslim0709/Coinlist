package com.kslim.coinlist.ui

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.kslim.coinlist.R
import com.kslim.coinlist.data.model.CoinCandle
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.databinding.ActivityCoinDetailsBinding
import com.kslim.coinlist.ui.viewmodel.CoinDetailsViewModel
import com.kslim.coinlist.utils.API
import com.kslim.coinlist.utils.Constants
import com.kslim.coinlist.utils.DateAxisValueFormatter
import com.kslim.coinlist.utils.numberFormat
import java.util.*

class CoinDetailsActivity : AppCompatActivity() {
    @Suppress("UNCHECKED_CAST")
    private val coinDetailsViewModel: CoinDetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                CoinDetailsViewModel() as T
        }
    }

    lateinit var coinDetailsBinding: ActivityCoinDetailsBinding
    lateinit var coinChart: CandleStickChart

    private var coinTicker: CoinInformation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        coinDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_coin_details)
        coinDetailsBinding.coinDetails = this@CoinDetailsActivity

        coinChart = coinDetailsBinding.chartCoin

        val coinTicker =
            intent.getParcelableExtra<CoinInformation>(Constants.INTENT_KEY_COIN_TICKER)
        this.coinTicker = coinTicker

        Glide.with(this@CoinDetailsActivity)
            .load(API.ICON_BASE_URL + Constants.BTC)
            .placeholder(R.drawable.ic_baseline_default_icon)
            .error(R.drawable.ic_baseline_error)
            .fallback(R.drawable.ic_baseline_image_not_supported)
            .apply(RequestOptions().circleCrop()).into(coinDetailsBinding.ivDetailsBitcoinIcon)


        coinTicker?.run {
            val market =
                coinTicker.market.substring(coinTicker.market.lastIndexOf("-") + 1)
            Glide.with(this@CoinDetailsActivity)
                .load(API.ICON_BASE_URL + market.toLowerCase(Locale.getDefault()))
                .placeholder(R.drawable.ic_baseline_default_icon)
                .error(R.drawable.ic_baseline_error)
                .fallback(R.drawable.ic_baseline_image_not_supported)
                .apply(RequestOptions().circleCrop()).into(coinDetailsBinding.ivDetailsIcon)
            coinDetailsBinding.tvDetailsCoinNm.text = coinTicker.koreanName
            coinDetailsBinding.tvDetailsCoinPrice.text = coinTicker.tradePrice.numberFormat(0)
            coinDetailsBinding.tvDetailsCoinAmount.text =
                String.format("%,.0f백만", coinTicker.accTradePrice24h / 1000000.0)
        }

        initChart()
        initObserve()

        coinTicker?.market?.let {
            coinDetailsViewModel.checkIsFavoriteCoin(it)
            if (!coinTicker.market.equals(Constants.BTC_TICKER, true)) {
                coinDetailsViewModel.requestAllCoinTicker(Constants.BTC_TICKER + "," + it)
            } else {
                coinDetailsViewModel.requestAllCoinTicker(Constants.BTC_TICKER)
            }

        }

    }

    private fun initChart() {

        coinChart.setBorderColor(Color.WHITE)
        coinChart.description.isEnabled = false

        coinChart.setPinchZoom(false)
        coinChart.setDrawGridBackground(false)

        coinChart.xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM
            setLabelCount(4, true)
            valueFormatter = DateAxisValueFormatter()
            setDrawGridLines(false)
            setAvoidFirstLastClipping(true)
        }

        coinChart.axisLeft.run {
            setLabelCount(4, true)
            setDrawGridLines(false)
            //setDrawAxisLine(false)
        }

        coinChart.axisRight.run {
            isEnabled = false
        }

        coinChart.setTouchEnabled(false)
        coinChart.legend.isEnabled = false
    }

    private fun initObserve() {
        // Result favorite Coin insert and delete, change Favorite Button resource
        coinDetailsViewModel.isSuccess.observe(this, {
            coinDetailsBinding.cbDetailsFavorite.isChecked = it
            if (it) {
                coinDetailsBinding.cbDetailsFavorite.setButtonDrawable(R.drawable.ic_baseline_favorite)
            } else {
                coinDetailsBinding.cbDetailsFavorite.setButtonDrawable(R.drawable.ic_baseline_favorite_border)
            }
        })

        // Start Activity set Favorite button init
        coinDetailsViewModel.isCheck.observe(this, {
            when (it) {
                0 -> {
                    coinDetailsBinding.cbDetailsFavorite.isChecked = false
                    coinDetailsBinding.cbDetailsFavorite.setButtonDrawable(R.drawable.ic_baseline_favorite_border)
                }
                1 -> {
                    coinDetailsBinding.cbDetailsFavorite.isChecked = true
                    coinDetailsBinding.cbDetailsFavorite.setButtonDrawable(R.drawable.ic_baseline_favorite)
                }
            }

        })
        // Update Coin Ticker in coinDetailAdapter
        coinDetailsViewModel.coinTickerList.observe(this, {
            it.forEach { coinTicker ->
                if (coinTicker.market.equals(Constants.BTC_TICKER, true)) {
                    coinDetailsBinding.tvDetailsBitcoinPrice.text =
                        coinTicker.tradePrice.numberFormat(0)
                    coinDetailsBinding.tvDetailsBitcoinPrice.setTextColor(
                        ContextCompat.getColor(
                            this,
                            com.kslim.coinlist.utils.getColor(coinTicker.change)
                        )
                    )
                } else {
                    coinDetailsBinding.tvDetailsCoinPrice.text =
                        coinTicker.tradePrice.numberFormat(0)
                    coinDetailsBinding.tvDetailsCoinPrice.setTextColor(
                        ContextCompat.getColor(
                            this,
                            com.kslim.coinlist.utils.getColor(coinTicker.change)
                        )
                    )
                    coinDetailsBinding.tvDetailsCoinAmount.text =
                        String.format("%,.0f백만", coinTicker.accTradePrice24h / 1000000.0)
                }
            }
        })

        coinDetailsViewModel.coinDayCandles.observe(this, { dayCandles ->
            if (!dayCandles.isNullOrEmpty()) {

                makeCandleChart(dayCandles.sortedBy { it.timestamp })
            }
        })
    }

    private fun makeCandleChart(coinDayCandles: List<CoinCandle>) {


        val entries = ArrayList<CandleEntry>()


        for (coinDayCandle in coinDayCandles) {
            entries.add(
                CandleEntry(
                    coinDayCandle.timestamp.toFloat(),
                    coinDayCandle.highPrice.toFloat(),
                    coinDayCandle.lowPrice.toFloat(),
                    coinDayCandle.openingPrice.toFloat(),
                    coinDayCandle.tradePrice.toFloat()
                )
            )
        }

        val dataSet = CandleDataSet(entries, "DayCandles").apply {

            setDrawIcons(false)
            axisDependency = YAxis.AxisDependency.LEFT

            shadowColor = Color.LTGRAY
            shadowWidth = 1f

            // 음봉
            decreasingColor = Color.BLUE
            decreasingPaintStyle = Paint.Style.STROKE
            increasingColor = Color.RED
            increasingPaintStyle = Paint.Style.STROKE

            setDrawValues(false)
            highLightColor = Color.TRANSPARENT
        }
        coinChart.setMaxVisibleValueCount(Constants.CHART_BONG)
        val candleData = CandleData(dataSet)

        coinChart.data = candleData
        coinChart.invalidate()

    }

    // Click Favorite Button
    fun onClickFavorite() {
        when (coinDetailsBinding.cbDetailsFavorite.isChecked) {
            true -> {
                coinTicker?.market?.let { coinDetailsViewModel.deleteFavoriteCoin(it) }
            }
            false -> {
                coinTicker?.let { coinDetailsViewModel.insertFavoriteCoin(it) }
            }

        }
    }

    // 일봉 캔들 
    fun requestCoinDayCandles() {
        coinTicker?.market?.let {
            coinDetailsViewModel.requestCoinDayCandlesData(
                it,
                Constants.CHART_BONG
            )
        }
    }

    // 주봉 캔들
    fun requestCoinWeeksCandles() {
        coinTicker?.market?.let {
            coinDetailsViewModel.requestCoinWeeksCandlesData(
                it,
                Constants.CHART_BONG
            )
        }
    }

    // 월봉 캔들
    fun requestCoinMonthCandles() {
        coinTicker?.market?.let {
            coinDetailsViewModel.requestCoinMonthCandlesData(
                it,
                Constants.CHART_BONG
            )
        }
    }

    fun onClickClose() {
        finish()
    }
}