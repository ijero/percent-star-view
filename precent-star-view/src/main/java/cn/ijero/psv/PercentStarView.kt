package cn.ijero.psv

import android.content.Context
import android.graphics.*
import android.support.annotation.FloatRange
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import kotlin.math.cos
import kotlin.math.sin

class PercentStarView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), AnkoLogger {

    companion object {
        const val MIN_PLUMP = 0.3F
        const val DEF_PLUMP = 0.4F
        const val MAX_PLUMP = 0.8F
        const val INT_INVALID = -1
    }

    var count = 5
        set(value) {
            field = value
            invalidate()
        }

    var max = 100
        set(value) {
            field = value
            invalidate()
        }

    var progress = 60
        set(value) {
            field = value
            invalidate()
        }

    var starBackColor = Color.parseColor("#FFFFFFFF")
        set(value) {
            field = value
            invalidate()
        }

    var starProgressColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    var starStrokeWidth = 1
        set(value) {
            field = value
            invalidate()
        }

    var starStrokeColor = Color.parseColor("#FF666666")
        set(value) {
            field = value
            invalidate()
        }

    var starSize = INT_INVALID
        set(value) {
            field = value
            mOuterRadius = (field / 2).toFloat()
        }


    @FloatRange(from = MIN_PLUMP.toDouble(), to = MAX_PLUMP.toDouble())
    var plump = DEF_PLUMP
        set(value) {
            field = value
            mInnerRadius = mOuterRadius * field
        }

    private var mOuterRadius = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var mInnerRadius = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var mStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = starStrokeColor
        strokeWidth = this@PercentStarView.starStrokeWidth.toFloat()
    }

    private var mStarPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = strokeWidth
        color = starBackColor
    }


    private val mLayerRectF by lazy {
        RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        applyStyle(attrs)
        checkValues()
    }

    private fun checkValues() {
        if (count < 0) {
            throw IllegalArgumentException("The 'count' should > 0 .")
        }

        if (plump < MIN_PLUMP || plump > MAX_PLUMP) {
            throw IllegalArgumentException("The 'plump' must be range in '0.4' to '0.8'.")
        }

        if (progress < 0) {
            progress = 0
        }

        if (progress > max) {
            progress = max
        }

        if (starSize == INT_INVALID) {
            starSize = dip(30)
        }

        mOuterRadius = starSize.toFloat() * 0.5F
        mInnerRadius = mOuterRadius * plump


    }

    private fun applyStyle(attrs: AttributeSet?) {
        attrs ?: return

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PercentStarView)
        starBackColor = ta.getColor(R.styleable.PercentStarView_psv_starBackColor, starBackColor)
        starProgressColor = ta.getColor(R.styleable.PercentStarView_psv_starProgressColor, starProgressColor)
        starStrokeColor = ta.getColor(R.styleable.PercentStarView_psv_starStrokeColor, starStrokeColor)
        starStrokeWidth = ta.getDimensionPixelSize(R.styleable.PercentStarView_psv_starStrokeWidth, starStrokeWidth)
        count = ta.getInt(R.styleable.PercentStarView_psv_starCount, count)
        plump = ta.getFloat(R.styleable.PercentStarView_psv_starPlump, plump)
        starSize = ta.getDimensionPixelSize(R.styleable.PercentStarView_psv_starSize, starSize)
        max = ta.getInt(R.styleable.PercentStarView_psv_max, max)
        progress = ta.getInt(R.styleable.PercentStarView_psv_progress, progress)

        ta.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getWidthSize(widthMeasureSpec), getHeightSize(heightMeasureSpec))
    }

    private fun getWidthSize(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return starSize * count + paddingLeft + paddingRight
        }

        return size
    }

    private fun getHeightSize(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return starSize + paddingBottom + paddingTop
        }

        return size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val saveLayer = canvas.saveLayer(mLayerRectF, mStarPaint, Canvas.ALL_SAVE_FLAG)
        // starBack
        canvas.translate(0F, starSize * 0.05f) // 减少上下和左右的间距差
        canvas.save()
        mStarPaint.color = starBackColor
        canvas.drawStars(mStarPaint)
        canvas.restore()
        canvas.save()

        // stroke
        canvas.drawStars(mStrokePaint)
        canvas.restore()

        canvas.drawProgress()
        canvas.restoreToCount(saveLayer)
    }

    private fun Canvas.drawProgress() {
        mStarPaint.color = starProgressColor

        mStarPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        drawRect(RectF(paddingStart.toFloat(), paddingTop.toFloat(),
                (measuredWidth - paddingEnd).toFloat() - 240,
                (measuredHeight - paddingBottom).toFloat()), mStarPaint)

        mStarPaint.xfermode = null

    }

    private fun Canvas.drawStars(paint: Paint) {

        for (i in 0 until count) {
            if (i == 0) {
                translate(paddingStart.toFloat(), paddingTop.toFloat())
            } else {
                translate(starSize.toFloat(), 0F)
            }
            drawStar(paint)
        }

        translate(0F, 0F)
    }

    private fun Canvas.drawStar(paint: Paint) {
        Path().apply {
            // 绘制辅助半径线
            val outerPos = arrayListOf<PointF>()
            val innerPos = arrayListOf<PointF>()

            for (i in 0 until 5) {
                moveTo(mOuterRadius + measuredWidth, mOuterRadius)
                val dx = (mOuterRadius + mOuterRadius * cos((i * 72F - 18F) * Math.PI / 180F)).toFloat()
                val dy = (mOuterRadius + mOuterRadius * sin((i * 72F - 18F) * Math.PI / 180F)).toFloat()
                outerPos.add(PointF(dx, dy))
            }

            for (i in 0 until 5) {
                moveTo(mOuterRadius + measuredWidth, mOuterRadius)
                val dx = (mOuterRadius + mInnerRadius * cos((i * 72F + 18F) * Math.PI / 180F)).toFloat()
                val dy = (mOuterRadius + mInnerRadius * sin((i * 72F + 18F) * Math.PI / 180F)).toFloat()
                innerPos.add(PointF(dx, dy))
            }

            moveTo(outerPos[0].x, outerPos[0].y)
            lineTo(innerPos[0].x, innerPos[0].y)
            lineTo(outerPos[1].x, outerPos[1].y)
            lineTo(innerPos[1].x, innerPos[1].y)
            lineTo(outerPos[2].x, outerPos[2].y)
            lineTo(innerPos[2].x, innerPos[2].y)
            lineTo(outerPos[3].x, outerPos[3].y)
            lineTo(innerPos[3].x, innerPos[3].y)
            lineTo(outerPos[4].x, outerPos[4].y)
            lineTo(innerPos[4].x, innerPos[4].y)

            close()
            drawPath(this, paint)
        }

    }

    private fun getCanDrawingRect(): RectF {
        return RectF(paddingStart.toFloat(), paddingTop.toFloat(),
                (measuredWidth - paddingEnd).toFloat(),
                (measuredHeight - paddingBottom).toFloat())
    }


}
