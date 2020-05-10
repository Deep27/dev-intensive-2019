package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint()
    private val borderPaint: Paint = Paint()

    private var borderWidth: Int = DEFAULT_BORDER_WIDTH
    private var borderColor: Int = DEFAULT_BORDER_COLOR
    private var viewWidth: Float = 0f
    private var viewHeight: Float = 0f
    private var image: Bitmap? = null
    private var shader: BitmapShader? = null

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = 0xfff
    }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            borderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            a.recycle()
        }

        paint.setAntiAlias(true)
        setBorderColor(R.color.color_white)
        borderPaint.setAntiAlias(true)
        setLayerType(View.LAYER_TYPE_SOFTWARE, borderPaint)
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
        borderPaint.setColor(borderColor)
        invalidate()
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        borderPaint.setColor(borderColor)
        invalidate()
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidth

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {

        val bitmapDrawable = getDrawable()
        if (bitmapDrawable != null) {
            image = (bitmapDrawable as BitmapDrawable).getBitmap()
        }

        if (image != null) {
            shader = BitmapShader(
                Bitmap.createScaledBitmap(image!!, getWidth(), getHeight(), false),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
            )
            paint.setShader(shader)
            val circleCenter = viewWidth / 2

            if (canvas != null) {
                canvas.drawCircle(
                    circleCenter + borderWidth,
                    circleCenter + borderWidth,
                    circleCenter + borderWidth - 4.0f,
                    borderPaint
                )
                canvas.drawCircle(
                    circleCenter + borderWidth,
                    circleCenter + borderWidth,
                    circleCenter - 4.0f,
                    paint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec, widthMeasureSpec)

        viewWidth = ((width - (borderWidth * 2)).toFloat())
        viewHeight = (height - (borderWidth * 2)).toFloat()

        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return if (specMode == MeasureSpec.EXACTLY) {
            // we were told how big to be
            specSize
        } else {
            // measure the text
            viewWidth.toInt()
        }
    }

    private fun measureHeight(measureSpecHeight: Int, measureSpecWidth: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)

        return if (specMode == MeasureSpec.EXACTLY) {
            // we were told hom big to be
            specSize
        } else {
            // measure text (ascent is a negative number)
            viewHeight.toInt() + 2
        }
    }
}
