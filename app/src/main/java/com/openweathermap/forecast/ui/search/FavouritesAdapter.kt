package com.openweathermap.forecast.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.openweathermap.forecast.databinding.FavCityItemBinding
import com.openweathermap.forecast.persistance.Favourites
import com.openweathermap.forecast.utils.DataDiffUtil


class FavouritesAdapter(private val context: Context, private var dataList: MutableList<Favourites>,
                        private var onItemSelected: FavouriteCityNavigator) :
    RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FavCityItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    fun addData(favs: List<Favourites>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(DataDiffUtil(this.dataList, favs))
        dataList.clear()
        dataList.addAll(favs)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var itemRowBinding: FavCityItemBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        var binding: FavCityItemBinding = itemRowBinding

        fun bind(obj: Favourites) {
            itemRowBinding.setVariable(BR.item, obj)
            itemRowBinding.executePendingBindings()
            itemRowBinding.root.setOnClickListener {
                onItemSelected.onCitySelected(obj)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}