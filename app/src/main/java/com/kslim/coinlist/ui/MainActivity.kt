package com.kslim.coinlist.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.kslim.coinlist.R
import com.kslim.coinlist.databinding.ActivityMainBinding
import com.kslim.coinlist.ui.fragment.CoinExplainFragment
import com.kslim.coinlist.ui.fragment.CoinFavoriteFragment
import com.kslim.coinlist.ui.fragment.CoinListFragment
import com.kslim.coinlist.ui.viewmodel.SharedViewModel
import com.kslim.coinlist.utils.API
import com.kslim.coinlist.utils.Constants
import com.kslim.coinlist.utils.numberFormat
import com.kslim.coinlist.utils.onSearchTextChanged
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainDataBinding: ActivityMainBinding

    @Suppress("UNCHECKED_CAST")
    private val sharedViewModel: SharedViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = SharedViewModel() as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainDataBinding.mainActivity = this@MainActivity

        mainDataBinding.layoutTabs.addTab(
            mainDataBinding.layoutTabs.newTab().setText(getString(R.string.text_won)), false
        )
        mainDataBinding.layoutTabs.addTab(
            mainDataBinding.layoutTabs.newTab().setText(getString(R.string.text_favorite))
        )
        mainDataBinding.layoutTabs.addTab(
            mainDataBinding.layoutTabs.newTab().setText(getString(R.string.text_explanation))
        )

        mainDataBinding.layoutTabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    Constants.FG_COIN_LIST -> {
                        val coinListFragment = CoinListFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.layout_container, coinListFragment).commit()
                    }
                    Constants.FG_COIN_FAVORITE -> {
                        val coinFavoriteFragment = CoinFavoriteFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.layout_container, coinFavoriteFragment).commit()
                    }
                    Constants.FG_COIN_EXPLAIN -> {
                        val coinFavoriteFragment = CoinExplainFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.layout_container, coinFavoriteFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        mainDataBinding.layoutTabs.getTabAt(0)?.select()

        mainDataBinding.editSearchText.onSearchTextChanged {
            sharedViewModel.setEditText(it.toString())
        }

        Glide.with(this@MainActivity)
            .load(API.ICON_BASE_URL + Constants.BTC)
            .placeholder(R.drawable.ic_baseline_default_icon)
            .error(R.drawable.ic_baseline_error)
            .fallback(R.drawable.ic_baseline_image_not_supported)
            .apply(RequestOptions().circleCrop()).into(mainDataBinding.ivIcon)

        sharedViewModel.coinTickerList.observe(this, {
            mainDataBinding.tvBitcoinPrice.text = it.first().tradePrice.numberFormat(0)
            mainDataBinding.tvBitcoinPrice.setTextColor(
                ContextCompat.getColor(
                    this,
                    com.kslim.coinlist.utils.getColor(it.first().change)
                )
            )
        })
    }

    override fun onResume() {
        sharedViewModel.requestAllCoinTicker(Constants.BTC_TICKER)
        super.onResume()
    }

    override fun onPause() {
        sharedViewModel.stopRequestAppCoinTicker()
        super.onPause()
    }
}

