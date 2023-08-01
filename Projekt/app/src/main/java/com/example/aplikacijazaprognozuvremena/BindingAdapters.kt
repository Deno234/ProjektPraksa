package com.example.aplikacijazaprognozuvremena

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}