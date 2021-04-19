package com.kslim.coinlist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kslim.coinlist.R
import com.kslim.coinlist.data.model.CoinExplain
import com.kslim.coinlist.databinding.FragmentCoinExplainBinding
import com.kslim.coinlist.ui.adapter.CoinExplainAdapter
import com.kslim.coinlist.ui.viewmodel.SharedViewModel
import com.kslim.coinlist.utils.Constants
import java.util.*

class CoinExplainFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = SharedViewModel() as T
        }
    }

    private lateinit var coinExplainBinding: FragmentCoinExplainBinding

    private lateinit var coinExplainAdapter: CoinExplainAdapter
    private lateinit var coinExplainRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        coinExplainBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_coin_explain,
            container,
            false
        )

        coinExplainBinding.coinExplainFragment = this@CoinExplainFragment
        return coinExplainBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val markets =
//            CoinDataSet.getInstance().getCoinInformationList().subList(0, 40)
//                .filter { coinInformation ->
//                    coinInformation.market.contains("-")
//                }.map {
//                    it.market.substring(it.market.lastIndexOf("-") + 1)
//                }.toList().joinToString(",")
//
//        Log.v(
//            "Explain",
//            "onViewCreate: ${markets}, size: ${
//                CoinDataSet.getInstance().getCoinInformationList().size
//            }"
//        )

        coinExplainAdapter = CoinExplainAdapter()
        coinExplainRecyclerView = coinExplainBinding.recyclerExplainCoin

        coinExplainRecyclerView.adapter = coinExplainAdapter

        sharedViewModel.explainCoinList.observe(viewLifecycleOwner, {
            Log.v(Constants.TAG_MAIN, "Favorite favoriteCoinTickerList")
            coinExplainAdapter.setCoinExplainList(it as ArrayList<CoinExplain>)
        })

        sharedViewModel.getDBAllCoinExplain()
    }

}