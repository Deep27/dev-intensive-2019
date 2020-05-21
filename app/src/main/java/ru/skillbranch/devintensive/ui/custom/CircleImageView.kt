package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_BORDER_COLOR = Color.WHITE // R.attr.colorAccent
        private const val DEFAULT_SIZE = 40
    }

    @Px
    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)

    @ColorInt
    private var borderColor: Int = Color.WHITE
    private var initials: String = "??"

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()

    private var isAvatarMode = true

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = ta.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                context.dpToPx(DEFAULT_BORDER_WIDTH)
            )
            borderColor = ta.getColor(
                R.styleable.CircleImageView_cv_borderColor,
                DEFAULT_BORDER_BORDER_COLOR
            )
            initials = ta.getString(R.styleable.CircleImageView_cv_initials) ?: "??"
            ta.recycle()
        }

        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(initSize, initSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)

        if (drawable != null && isAvatarMode) drawAvatar(canvas)
        else drawInitials(canvas)

        // resize rect
        val half = (borderWidth / 2).toInt()
        with(borderRect) {
            set(viewRect)
            inset(half, half)
        }
        canvas.drawOval(RectF(borderRect), borderPaint)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (isAvatarMode) prepareShader(width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isAvatarMode) prepareShader(width, height)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (isAvatarMode) prepareShader(width, height)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isAvatarMode = isAvatarMode
        savedState.borderWidth = borderWidth
        savedState.borderColor = borderColor
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
//        super.onRestoreInstanceState(state) // or here ?
        if (state is SavedState) {
            state.also { // ??
                isAvatarMode = state.isAvatarMode
                borderWidth = state.borderWidth
                borderColor = state.borderColor
            }

            with(borderPaint) {
                color = borderColor
                strokeWidth = borderWidth
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    fun setInitials(initials: String) {
        this.initials = initials
        if (!isAvatarMode) invalidate()
    }

    fun setBorderColor(@ColorInt color: Int) {
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderWidth(@Dimension width: Int) {
        borderWidth = context.dpToPx(width)
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }

    private fun setup() {
        with(borderPaint) {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }
    }

    private fun prepareShader(w: Int, h: Int) {
        if (w == 0 || drawable == null) return
        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(RectF(viewRect), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas) {
//        initialsPaint.color = Color.WHITE
        canvas.drawOval(RectF(viewRect), initialsPaint)
        with(initialsPaint) {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }

        val offsetY = (initialsPaint.descent() + initialsPaint.ascent()) / 2
        canvas.drawText(
            initials,
            viewRect.exactCenterX(),
            viewRect.exactCenterY() - offsetY,
            initialsPaint
        )
    }

    private class SavedState : BaseSavedState, Parcelable {

        var isAvatarMode: Boolean = true
        var borderWidth: Float = 0f
        var borderColor: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(src: Parcel) : super(src) {
            isAvatarMode = src.readInt() == 1
            borderWidth = src.readFloat()
            borderColor = src.readInt()
        }

        override fun writeToParcel(dst: Parcel, flags: Int) {
            super.writeToParcel(dst, flags)
            dst.writeInt(if (isAvatarMode) 1 else 0)
            dst.writeFloat(borderWidth)
            dst.writeInt(borderColor)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}

fun Context.dpToPx(dp: Int): Float = dp.toFloat() * this.resources.displayMetrics.density


//package ru.skillbranch.devintensive.ui.custom
//
//import android.content.Context
//import android.graphics.*
//import android.util.AttributeSet
//import androidx.annotation.ColorInt
//import androidx.annotation.Px
//import androidx.appcompat.widget.AppCompatImageView
//import androidx.core.graphics.drawable.toBitmap
//import ru.skillbranch.devintensive.R
//
//class AvatarImageViewMask @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : AppCompatImageView(context, attrs, defStyleAttr) {
//
//    companion object {
//        private const val DEFAULT_BORDER_WIDTH = 2
//        private const val DEFAULT_BORDER_BORDER_COLOR = Color.WHITE // R.attr.colorAccent
//        private const val DEFAULT_SIZE = 40
//    }
//
//    @Px
//    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
//    @ColorInt
//    private var borderColor: Int = Color.WHITE
//    private var initials: String = "??"
//
//    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val viewRect = Rect()
//    private lateinit var resultBm: Bitmap
//    private lateinit var maskBm: Bitmap
//    private lateinit var srcBm: Bitmap
//
//    init {
//        if (attrs != null) {
//            val ta = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageViewMask)
//            borderWidth = ta.getDimension(R.styleable.AvatarImageViewMask_aiv_borderWidth, context.dpToPx(DEFAULT_BORDER_WIDTH))
//            borderColor = ta.getColor(R.styleable.AvatarImageViewMask_aiv_borderColor, DEFAULT_BORDER_BORDER_COLOR)
//            initials = ta.getString(R.styleable.AvatarImageViewMask_aiv_initials) ?: "??"
//            ta.recycle()
//        }
//
//        scaleType = ScaleType.CENTER_CROP
//        setup()
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        val initSize = resolveDefaultSize(widthMeasureSpec)
//        setMeasuredDimension(initSize, initSize)
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//
//        if (w == 0) return
//        with(viewRect) {
//            left = 0
//            top = 0
//            right = w
//            bottom = h
//        }
//        prepareBitmaps(w, h)
//    }
//
//    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
//        // NOT allocate, ONLY draw
//        canvas.drawBitmap(resultBm, viewRect, viewRect, null)
//        // resize rect
//        val half = (borderWidth / 2).toInt()
//        viewRect.inset(half, half)
//        canvas.drawOval(RectF(viewRect), borderPaint)
//    }
//
//    private fun setup() {
//        with(maskPaint) {
//            color = Color.RED
//            style = Paint.Style.FILL
//        }
//        with(borderPaint) {
//            style = Paint.Style.STROKE
//            strokeWidth = borderWidth
//            color = borderColor
//        }
//    }
//
//    private fun prepareBitmaps(w: Int, h: Int) {
//        maskBm = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
//        resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)
//        val maskCanvas = Canvas(maskBm)
//        maskCanvas.drawOval(RectF(viewRect), maskPaint)
//        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
//
//        val resultCanvas = Canvas(resultBm)
//        resultCanvas.drawBitmap(maskBm, viewRect, viewRect, null)
//        resultCanvas.drawBitmap(srcBm, viewRect, viewRect, maskPaint)
//    }
//
//    private fun resolveDefaultSize(spec: Int): Int {
//        return when (MeasureSpec.getMode(spec)) {
//            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
//            else -> MeasureSpec.getSize(spec)
//        }
//    }
//
//}


//package ru.skillbranch.devintensive.ui.custom
//
//import android.content.Context
//import android.graphics.*
//import android.util.AttributeSet
//import androidx.annotation.ColorInt
//import androidx.annotation.Px
//import androidx.appcompat.widget.AppCompatImageView
//import androidx.core.graphics.drawable.toBitmap
//import ru.skillbranch.devintensive.R
//
//class AvatarImageViewShader @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : AppCompatImageView(context, attrs, defStyleAttr) {
//
//    companion object {
//        private const val DEFAULT_BORDER_WIDTH = 2
//        private const val DEFAULT_BORDER_BORDER_COLOR = Color.WHITE // R.attr.colorAccent
//        private const val DEFAULT_SIZE = 40
//    }
//
//    @Px
//    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
//    @ColorInt
//    private var borderColor: Int = Color.WHITE
//    private var initials: String = "??"
//
//    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val viewRect = Rect()
//
//    init {
//        if (attrs != null) {
//            val ta = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageViewShader)
//            borderWidth = ta.getDimension(R.styleable.AvatarImageViewShader_aivs_borderWidth, context.dpToPx(DEFAULT_BORDER_WIDTH))
//            borderColor = ta.getColor(R.styleable.AvatarImageViewShader_aivs_borderColor, DEFAULT_BORDER_BORDER_COLOR)
//            initials = ta.getString(R.styleable.AvatarImageViewShader_aivs_initials) ?: "??"
//            ta.recycle()
//        }
//
//        scaleType = ScaleType.CENTER_CROP
//        setup()
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        val initSize = resolveDefaultSize(widthMeasureSpec)
//        setMeasuredDimension(initSize, initSize)
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//
//        if (w == 0) return
//        with(viewRect) {
//            left = 0
//            top = 0
//            right = w
//            bottom = h
//        }
//        prepareShader(w, h)
//    }
//
//    override fun onDraw(canvas: Canvas) {
////        super.onDraw(canvas)
//
//        canvas.drawOval(RectF(viewRect), avatarPaint)
//
//        val half = (borderWidth / 2).toInt()
//        viewRect.inset(half, half)
//        canvas.drawOval(RectF(viewRect), borderPaint)
//    }
//
//    private fun setup() {
//        with(borderPaint) {
//            style = Paint.Style.STROKE
//            strokeWidth = borderWidth
//            color = borderColor
//        }
//    }
//
//    private fun prepareShader(w: Int, h: Int) {
//        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
//        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//    }
//
//    private fun resolveDefaultSize(spec: Int): Int {
//        return when (MeasureSpec.getMode(spec)) {
//            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt()
//            else -> MeasureSpec.getSize(spec)
//        }
//    }
//}
