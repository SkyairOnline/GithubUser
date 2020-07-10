package com.arudo.githubconsumer.animation

import android.content.Context
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.arudo.githubconsumer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FloatingActionIconAnimation(context: Context) {
    var floatActionButton: FloatingActionButton? = null
    private val favoriteBorder = AnimatedVectorDrawableCompat.create(
        context,
        R.drawable.ic_favorite_border
    )
    private val favorite = AnimatedVectorDrawableCompat.create(
        context,
        R.drawable.ic_favorite
    )

    fun icon(isState: Boolean) {
        val nextAnimation = if (isState) favorite else favoriteBorder
        floatActionButton?.setImageDrawable(nextAnimation)
    }

    fun animate(isState: Boolean) {
        val nextAnimation = if (isState) favorite else favoriteBorder
        animation(nextAnimation)
    }

    private fun animation(drawable: AnimatedVectorDrawableCompat?) {
        floatActionButton?.let {
            drawable?.start()
        }
    }
}