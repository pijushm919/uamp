package com.example.android.uamp.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.android.uamp.R

@BindingAdapter("setImage")
fun ImageView.setImage(imageUrl: Uri){
    imageUrl.let{
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.default_art)
            .into(this)
    }
}