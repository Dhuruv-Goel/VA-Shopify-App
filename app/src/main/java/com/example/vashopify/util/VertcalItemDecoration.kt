package com.example.vashopify.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.time.temporal.TemporalAmount

class VertcalItemDecoration(private val amount: Int =30):RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom= amount
    }
}