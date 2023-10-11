package com.example.playlistmaker.presentation.library.playlists

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (column == 0) {
            outRect.left = horizontalSpacing  // Отступ для первого столбца слева
            outRect.right = horizontalSpacing / 2
        } else {
            outRect.left = horizontalSpacing / 2
            outRect.right = horizontalSpacing  // Отступ для последнего столбца справа
        }

        if (position >= spanCount) {
            outRect.top = verticalSpacing
        }
    }
}