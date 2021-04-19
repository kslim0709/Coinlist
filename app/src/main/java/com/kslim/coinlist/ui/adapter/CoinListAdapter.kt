package com.kslim.coinlist.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kslim.coinlist.R
import com.kslim.coinlist.data.model.CoinInformation
import com.kslim.coinlist.databinding.ItemCoinBinding
import com.kslim.coinlist.ui.CoinDetailsActivity
import com.kslim.coinlist.utils.API
import com.kslim.coinlist.utils.Constants
import com.kslim.coinlist.utils.getColor
import com.kslim.coinlist.utils.numberFormat
import java.util.*
import java.util.regex.Pattern

class CoinListAdapter : RecyclerView.Adapter<CoinListAdapter.CoinListViewHolder>() {

    private var coinTickerList: ArrayList<CoinInformation> = arrayListOf()
    private var copyCoinTickerList: ArrayList<CoinInformation> = arrayListOf()

    fun setCoinTickerList(coinTickerList: List<CoinInformation>) {
        this.coinTickerList = coinTickerList as ArrayList<CoinInformation>
        this.copyCoinTickerList.addAll(coinTickerList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_coin,
            parent,
            false
        ) as ItemCoinBinding
        return CoinListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) =
        holder.onBind(coinTickerList[position], position)

    override fun getItemCount(): Int = coinTickerList.size

    fun searchFilterList(filter: String) {

        if (coinTickerList.size > 0) coinTickerList.clear()

        for (coinTicker in copyCoinTickerList) {
            if (Pattern.matches("^[a-zA-Z]*$", filter)) {
                if (coinTicker.englishName.contains(filter, true)) {
                    coinTickerList.add(coinTicker)
                }
            } else {
                if (coinTicker.koreanName.contains(filter)) {
                    coinTickerList.add(coinTicker)
                }
            }
        }
        if (filter.isEmpty() && coinTickerList.isEmpty()) {
            coinTickerList.addAll(copyCoinTickerList)
        }
        notifyDataSetChanged()
    }


    inner class CoinListViewHolder(private val coinItemBinding: ItemCoinBinding) :
        RecyclerView.ViewHolder(coinItemBinding.root) {


        fun onBind(coinInfo: CoinInformation, position: Int) {

            val market =
                coinInfo.market.substring(coinInfo.market.lastIndexOf("-") + 1)
            Glide.with(coinItemBinding.root)
                .load(API.ICON_BASE_URL + market.toLowerCase(Locale.getDefault()))
                .placeholder(R.drawable.ic_baseline_default_icon)
                .error(R.drawable.ic_baseline_error)
                .fallback(R.drawable.ic_baseline_image_not_supported)
                .apply(RequestOptions().circleCrop()).into(coinItemBinding.ivIcon)
            coinItemBinding.tvEnName.text = coinInfo.englishName
            coinItemBinding.tvKrName.text = coinInfo.koreanName

            coinItemBinding.tvCurPrice.setTextColor(ContextCompat.getColor(coinItemBinding.root.context, getColor(coinInfo.change)))

//            when (coinInfo.change) {
//                Constants.EVEN -> {
//                    coinItemBinding.tvCurPrice.setTextColor(
//                        ContextCompat.getColor(
//                            coinItemBinding.root.context,
//                            R.color.black
//                        )
//                    )
//                }
//                Constants.FALL -> {
//                    coinItemBinding.tvCurPrice.setTextColor(
//                        ContextCompat.getColor(
//                            coinItemBinding.root.context,
//                            R.color.color_00000FF
//                        )
//                    )
//                }
//                Constants.RISE -> {
//                    coinItemBinding.tvCurPrice.setTextColor(
//                        ContextCompat.getColor(
//                            coinItemBinding.root.context,
//                            R.color.color_FF0000
//                        )
//                    )
//                }
//            }


            coinItemBinding.tvCurPrice.text = coinInfo.tradePrice.numberFormat(0)
            coinItemBinding.tvTransactionAmount.text =
                String.format("%,.0f백만", coinInfo.accTradePrice24h / 1000000.0)

            coinItemBinding.layoutItemCoin.setOnClickListener { v ->
                val intent = Intent(v.context, CoinDetailsActivity::class.java).also {
                    it.putExtra(Constants.INTENT_KEY_COIN_TICKER, coinInfo)
                }
                v.context.startActivity(intent)

            }

        }


    }
}