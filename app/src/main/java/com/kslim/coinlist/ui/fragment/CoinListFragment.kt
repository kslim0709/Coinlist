package com.kslim.coinlist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kslim.coinlist.R
import com.kslim.coinlist.data.CoinDataSet
import com.kslim.coinlist.databinding.FragmentCoinListBinding
import com.kslim.coinlist.ui.adapter.CoinListAdapter
import com.kslim.coinlist.ui.viewmodel.SharedViewModel


class CoinListFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = SharedViewModel() as T
        }
    }

    private lateinit var coinListBinding: FragmentCoinListBinding

    private lateinit var coinListAdapter: CoinListAdapter
    private lateinit var coinRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        sharedViewModel.editText.observe(viewLifecycleOwner, Observer {
            coinListAdapter.searchFilterList(it)
        })

    }
}