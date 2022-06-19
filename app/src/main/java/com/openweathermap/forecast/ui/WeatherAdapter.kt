package com.openweathermap.forecast.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.openweathermap.forecast.databinding.ForecastItemLayoutBinding
import com.openweathermap.forecast.models.Forecast

class WeatherAdapter(private val context: Context, private var dataList: MutableList<Forecast>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ForecastItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.bind(data)
    }

    inner class ViewHolder(private var itemRowBinding: ForecastItemLayoutBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        var binding: ForecastItemLayoutBinding = itemRowBinding

        fun bind(obj: Forecast) {
            itemRowBinding.setVariable(BR.item, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}