package com.kslim.coinlist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kslim.coinlist.R
import com.kslim.coinlist.data.model.CoinExplain
import com.kslim.coinlist.databinding.ItemCoinExplainBinding
import java.util.*

class CoinExplainAdapter : RecyclerView.Adapter<CoinExplainAdapter.CoinExplainViewHolder>() {

    private var coinExplainList = arrayListOf<CoinExplain>()

    fun setCoinExplainList(coinExplainList: ArrayList<CoinExplain>) {
        this.coinExplainList = coinExplainList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinExplainViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_coin_explain,
            parent,
            false
        ) as ItemCoinExplainBinding
        return CoinExplainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinExplainViewHolder, position: Int) =
        holder.onBind(coinExplainList[position])

    override fun getItemCount(): Int = coinExplainList.size


    inner class CoinExplainViewHolder(private val coinItemExplain: ItemCoinExplainBinding) :
        RecyclerView.ViewHolder(coinItemExplain.root) {


        fun onBind(coinExplain: CoinExplain) {

            coinExplain.run {
                Glide.with(coinItemExplain.root)
                    .load(coinExplain.logo)
                    .placeholder(R.drawable.ic_baseline_default_icon)
                    .error(R.drawable.ic_baseline_error)
                    .fallback(R.drawable.ic_baseline_image_not_supported)
                    .apply(RequestOptions().circleCrop()).into(coinItemExplain.ivIcon)

                coinItemExplain.tvKrName.text = coinExplain.symbol
                coinItemExplain.tvEnName.text = coinExplain.name
                coinItemExplain.tvCoinExplain.text = coinExplain.description
            }
        }
    }
}
