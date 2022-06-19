package com.openweathermap.forecast.ui.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.openweathermap.forecast.R
import com.openweathermap.forecast.databinding.ActivitySearchBinding
import com.openweathermap.forecast.models.City
import com.openweathermap.forecast.persistance.Favourites
import com.openweathermap.forecast.utils.ActivityStackManager
import com.openweathermap.forecast.utils.MyAppPreferences
import com.openweathermap.forecast.utils.Utils
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), FavouriteCityNavigator {

    @Inject
    lateinit var sharedPrefs: MyAppPreferences

    private val viewModel: FavouritesViewModel by viewModels()

    lateinit var viewBinding: ActivitySearchBinding

    lateinit var adapter: FavouritesAdapter

    private var favouriteCities = mutableListOf<Favourites>()

    private var selectedCity: City? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        setupUI()
        setupObservers()

    }

    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = FavouritesAdapter(this, favouriteCities, this)
        viewBinding.favouritesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                viewBinding.favouritesRecyclerView.context,
                (viewBinding.favouritesRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        viewBinding.favAdapter = adapter

        viewBinding.searchEt.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utils.hideKeyboard(this, v)
                selectedCity = Utils.getLocationFromAddress(this, viewBinding.searchEt.text.toString())
                if (selectedCity != null) {
                    viewBinding.selectedCityName.text = viewBinding.searchEt.text.toString()
                    viewBinding.selectedCityLayout.visibility = View.VISIBLE

                }
                return@OnEditorActionListener true
            }
            false
        })

        viewBinding.addToFav.setOnClickListener {
            viewBinding.selectedCityLayout.visibility = View.GONE
            selectedCity?.let { viewModel.insertCity(Favourites(null, selectedCity!!))}
        }
    }

    private fun setupObservers() {
        viewModel.cities.observe(this) { it ->
            if(it.isNotEmpty()) {
                adapter.apply {
                    addData(it)
                }
                viewBinding.favCitiesLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCitySelected(favCity: Favourites) {
        sharedPrefs.saveSelectedCity(favCity)
        Utils.sendBroadcast(this)
        finish()
    }
}