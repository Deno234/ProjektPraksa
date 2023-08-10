package com.example.aplikacijazaprognozuvremena.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun setSrc(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}