package com.example.playlistmaker.presentation.audioPlayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var isPlaying = false
    private val imageBitmapPlay: Bitmap?
    private val imageBitmapPause: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    var onPlayPauseClicked: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            0, 0
        ).apply {
            try {
                imageBitmapPlay = getDrawable(R.styleable.PlaybackButtonView_playIcon)?.toBitmap()
                imageBitmapPause = getDrawable(R.styleable.PlaybackButtonView_pauseIcon)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val bitmap = if (isPlaying) imageBitmapPause else imageBitmapPlay
        bitmap?.let { canvas?.drawBitmap(it, null, imageRect, null) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val sizeInDp = pxToDp(38,context)
        val desiredWidth = imageBitmapPlay?.width ?: sizeInDp
        val desiredHeight = imageBitmapPlay?.height ?: sizeInDp

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)

        imageRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                isPlaying = !isPlaying
                onPlayPauseClicked?.invoke()
                setPlayingState(isPlaying)
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    fun setPlayingState(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        invalidate()
    }

    private fun pxToDp(px: Int, context: Context): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }
}
