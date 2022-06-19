package com.openweathermap.forecast.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImage(image: ImageView, icon: String?) {

    val iconUrl = "http://openweathermap.org/img/wn/$icon@2x.png"
    if (iconUrl.isNotEmpty()) {
        Glide.with(image.context)
            .load(iconUrl)
            .into(image)
    }
}
