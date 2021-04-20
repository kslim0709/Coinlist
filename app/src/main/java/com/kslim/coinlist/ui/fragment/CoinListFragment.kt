package com.kslim.coinlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kslim.coinlist.R
import com.kslim.coinlist.data.CoinDataSet
import com.kslim.coinlist.databinding.FragmentCoinListBinding
import com.kslim.coinlist.ui.adapter.CoinListAdapter
import com.kslim.coinlist.ui.viewmodel.SharedViewModel


class CoinListFragment : Fragment(), View.OnClickListener {

    @Suppress("UNCHECKED_CAST")
    private val sharedViewModel: SharedViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = SharedViewModel() as T
        }
    }

    private lateinit var coinListBinding: FragmentCoinListBinding

    private lateinit var coinListAdapter: CoinListAdapter
    private lateinit var coinRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        coinListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_coin_list,
            container,
            false
        )

        coinListBinding.coinListFragment = this@CoinListFragment

        return coinListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinListAdapter = CoinListAdapter()
        coinListAdapter.setCoinTickerList(CoinDataSet.getInstance().getCoinInformationList())
        coinRecyclerView = coinListBinding.recyclerCoinTicker

        coinRecyclerView.adapter = coinListAdapter

        sharedViewModel.editText.observe(viewLifecycleOwner, {
            coinListAdapter.searchFilterList(it)
        })

        coinListBinding.icItemCoinHead.findViewById<TextView>(R.id.tv_coin_name)
            .setOnClickListener(this)
        coinListBinding.icItemCoinHead.findViewById<TextView>(R.id.tv_cur_price)
            .setOnClickListener(this)
        coinListBinding.icItemCoinHead.findViewById<TextView>(R.id.tv_transaction_amount)
            .setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.tv_cur_price) {
            if (v.tag.equals("false")) {
                v.tag = "true"
                coinListAdapter.sortByCoinPrice(false)
            } else {
                v.tag = "false"
                coinListAdapter.sortByCoinPrice(true)
            }
        } else if (v?.id == R.id.tv_coin_name) {
            if (v.tag.equals("false")) {
                v.tag = "true"
                coinListAdapter.sortByCoinName(false)
            } else {
                v.tag = "false"
                coinListAdapter.sortByCoinName(true)
            }
        } else if (v?.id == R.id.tv_transaction_amount) {
            if (v.tag.equals("false")) {
                v.tag = "true"
                coinListAdapter.sortByCoinAmount(false)
            } else {
                v.tag = "false"
                coinListAdapter.sortByCoinAmount(true)
            }
        }
    }
}

