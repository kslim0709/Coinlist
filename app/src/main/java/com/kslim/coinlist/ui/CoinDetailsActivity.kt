package com.kslim.coinlist.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kslim.coinlist.R
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.data.model.CoinTicker
import com.kslim.coinlist.databinding.ActivityCoinDetailsBinding
import com.kslim.coinlist.ui.viewmodel.CoinDetailsViewModel
import com.kslim.coinlist.utils.API
import com.kslim.coinlist.utils.Constants
import com.kslim.coinlist.utils.numberFormat
import java.util.*

class CoinDetailsActivity : AppCompatActivity() {

    private val coinDetailsViewModel: CoinDetailsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                CoinDetailsViewModel() as T
        }
    }

    lateinit var coinDetailsBinding: ActivityCoinDetailsBinding
    private var coinTicker: CoinInformation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        coinDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_coin_details)
        coinDetailsBinding.coinDetails = this@CoinDetailsActivity

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
        coinTicker?.market?.let {
            coinDetailsViewModel.checkIsFavoriteCoin(it)
            if (!coinTicker.market.equals(Constants.BTC_TICKER, true)) {
                coinDetailsViewModel.requestAllCoinTicker(Constants.BTC_TICKER + "," + it)
            } else {
                coinDetailsViewModel.requestAllCoinTicker(Constants.BTC_TICKER)
            }
        }

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

    fun onClickClose() {
        finish()
    }
}