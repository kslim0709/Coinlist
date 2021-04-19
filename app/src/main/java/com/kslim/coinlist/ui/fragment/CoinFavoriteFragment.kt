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
import com.kslim.coinlist.databinding.FragmentCoinFavoriteBinding
import com.kslim.coinlist.ui.adapter.CoinFavoriteListAdapter
import com.kslim.coinlist.ui.viewmodel.SharedViewModel
import com.kslim.coinlist.utils.Constants

class CoinFavoriteFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = SharedViewModel() as T
        }
    }

    private lateinit var favoriteCoinBinding: FragmentCoinFavoriteBinding

    private lateinit var favoriteCoinListAdapter: CoinFavoriteListAdapter
    private lateinit var favoriteCoinRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        favoriteCoinBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_coin_favorite,
            container,
            false
        )

        favoriteCoinBinding.coinFavoriteFragment = this@CoinFavoriteFragment
        return favoriteCoinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(Constants.TAG_MAIN, "Favorite onViewCreated")
        favoriteCoinListAdapter = CoinFavoriteListAdapter()
        favoriteCoinRecyclerView = favoriteCoinBinding.recyclerFavoriteCoinTicker

        favoriteCoinRecyclerView.adapter = favoriteCoinListAdapter

        sharedViewModel.favoriteCoinTickerList.observe(viewLifecycleOwner, {
            favoriteCoinListAdapter.setFavoriteCoinTickerList(it)
        })

        sharedViewModel.editText.observe(viewLifecycleOwner, {
            favoriteCoinListAdapter.searchFilterList(it)
        })
        sharedViewModel.getAllFavoriteCoinList()
    }

    override fun onPause() {
        Log.v(Constants.TAG_MAIN, "Favorite onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.v(Constants.TAG_MAIN, "Favorite onResume")

        super.onResume()
    }

    override fun onDestroy() {
        Log.v(Constants.TAG_MAIN, "Favorite onDestroy")
        super.onDestroy()
    }
}