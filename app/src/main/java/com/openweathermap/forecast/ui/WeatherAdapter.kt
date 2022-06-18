package com.openweathermap.forecast.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openweathermap.forecast.databinding.ForecastItemLayoutBinding
import com.openweathermap.forecast.models.Forecast
import com.openweathermap.forecast.utils.DataDiffUtil

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

    fun addData(newForecasts: MutableList<Forecast>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(DataDiffUtil(this.dataList, newForecasts))
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var itemRowBinding: ForecastItemLayoutBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        var binding: ForecastItemLayoutBinding = itemRowBinding

        fun bind(obj: Forecast) {

            binding.day.text = obj.day
            val high = "H: " + obj.max.toInt().toString()
            binding.max.text = high
            val low = "L: " + obj.min.toInt().toString()
            binding.min.text = low
            val iconUrl = "http://openweathermap.org/img/wn/" + obj.icon + "@2x.png"
            Glide.with(context)
                .load(iconUrl)
                .into(binding.iconForecast)

            itemRowBinding.setVariable(BR.item, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}