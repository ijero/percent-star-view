package cn.ijero.psv

import android.content.Context
import android.graphics.*
import android.support.annotation.FloatRange
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class PercentStarView
@JvmOverloads
constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), AnkoLogger {

    companion object {
        const val MIN_BMI = 0.2F
        const val DEF_BMI = 0.4F
        const val MAX_BMI = 0.8F
        const val INT_INVALID = -1
    }

    private var mCount = 5
        set(value) {
            field = value
            requestLayout()
            postInvalidate()
        }

    private var mMax = 100
        set(value) {
            field = value
            postInvalidate()
        }

    private var mProgress = 60
        set(value) {
            field = value
            postInvalidate()
        }

    private var mStarBackColor = Color.parseColor("#FFFFFFFF")
        set(value) {
            field = value
            postInvalidate()
        }

    private var mStarProgressColor = Color.RED
        set(value) {
            field = value
            postInvalidate()
        }

    private var mStarStrokeWidth = 1F
        set(value) {
            field = value
            mStrokePaint.strokeWidth = field
            requestLayout()
            postInvalidate()
        }

    private var mStarStrokeColor = Color.parseColor("#FF666666")
        set(value) {
            field = value
            mStrokePaint.color = field
            postInvalidate()
        }

    private var mStarSize = INT_INVALID
        set(value) {
            field = value
            refreshStar()
        }

    @FloatRange(from = MIN_BMI.toDouble(), to = MAX_BMI.toDouble())
    private var mBmi = DEF_BMI
        set(value) {
            field = when {
                value < MIN_BMI -> MIN_BMI
                value > MAX_BMI -> MAX_BMI
                else -> value
            }
            refreshStar()
        }

    private var mIsStrokeOverride = false
        set(value) {
            field = value
            postInvalidate()
        }

    private var mOuterRadius = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    private var mInnerRadius = 0F
        set(value) {
            field = value
            postInvalidate()
        }

    private var mStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = mStarStrokeColor
        strokeWidth = this@PercentStarView.mStarStrokeWidth
    }

    private var mStarPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = strokeWidth
        color = mStarBackColor
    }

    private val mLayerRectF by lazy {
        RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    init {
        applyStyle(attrs)
        checkValues()
    }

    private fun refreshStar() {
        mOuterRadius = (mStarSize / 2).toFloat()
        mInnerRadius = mOuterRadius * mBmi
        requestLayout()
    }

    private fun checkValues() {
        if (mCount < 0) {
            throw IllegalArgumentException("The 'mCount' should > 0 .")
        }

        if (mBmi < MIN_BMI || mBmi > MAX_BMI) {
            throw IllegalArgumentException("The 'mBmi' must be range in '0.4' to '0.8'.")
        }

        if (mProgress < 0) {
            mProgress = 0
        }

        if (mProgress > mMax) {
            mProgress = mMax
        }

        if (mStarSize == INT_INVALID) {
            mStarSize = dip(30)
        }

        mOuterRadius = mStarSize.toFloat() * 0.5F
        mInnerRadius = mOuterRadius * mBmi

    }

    private fun applyStyle(attrs: AttributeSet?) {
        attrs ?: return

        val ta = context.obtainStyledAttributes(attrs, R.styleable.PercentStarView)
        mStarBackColor = ta.getColor(R.styleable.PercentStarView_psv_starBackColor, mStarBackColor)
        mStarProgressColor = ta.getColor(R.styleable.PercentStarView_psv_starProgressColor, mStarProgressColor)
        mStarStrokeColor = ta.getColor(R.styleable.PercentStarView_psv_starStrokeColor, mStarStrokeColor)
        mStarStrokeWidth = ta.getDimensionPixelSize(R.styleable.PercentStarView_psv_starStrokeWidth, mStarStrokeWidth.toInt()).toFloat()
        mCount = ta.getInt(R.styleable.PercentStarView_psv_starCount, mCount)
        mBmi = ta.getFloat(R.styleable.PercentStarView_psv_starBMI, mBmi)
        mStarSize = ta.getDimensionPixelSize(R.styleable.PercentStarView_psv_starSize, mStarSize)
        mMax = ta.getInt(R.styleable.PercentStarView_psv_max, mMax)
        mProgress = ta.getInt(R.styleable.PercentStarView_psv_progress, mProgress)
        mIsStrokeOverride = ta.getBoolean(R.styleable.PercentStarView_psv_isStrokeOverride, mIsStrokeOverride)

        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getWidthSize(widthMeasureSpec), getHeightSize(heightMeasureSpec))
    }

    private fun getWidthSize(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return mStarSize * mCount + paddingRight
        }
        return min(size, context.resources.displayMetrics.widthPixels)
    }

    private fun getHeightSize(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return mStarSize + paddingBottom + paddingTop
        }
        return min(size, context.resources.displayMetrics.heightPixels)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mLayerRectF.right = measuredWidth.toFloat()
        mLayerRectF.bottom = measuredHeight.toFloat()

        val saveLayer = canvas.saveLayer(mLayerRectF, mStarPaint, Canvas.ALL_SAVE_FLAG)

        // starBack
        canvas.translate(0F, mStarSize * 0.05f) // 减少上下和左右的间距差

        canvas.save()
        mStarPaint.color = mStarBackColor
        canvas.drawStars(mStarPaint)
        canvas.restore()

        // stroke
        if (!mIsStrokeOverride) {
            canvas.save()
            canvas.drawStars(mStrokePaint)
            canvas.restore()
        }

        canvas.save()
        mStarPaint.color = mStarProgressColor
        canvas.drawProgress()
        canvas.restore()

        // stroke
        if (mIsStrokeOverride) {
            canvas.save()
            canvas.drawStars(mStrokePaint)
            canvas.restore()
        }

        canvas.restoreToCount(saveLayer)
    }

    private fun Canvas.drawProgress() {
        mStarPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val progress = this@PercentStarView.mProgress.toFloat() / this@PercentStarView.mMax.toFloat()
        drawRect(RectF(paddingStart.toFloat() - mStarStrokeWidth, paddingTop.toFloat() - mStarStrokeWidth,
                (mStarSize * mCount + paddingLeft + paddingRight) * progress + (1 - progress) * paddingEnd - paddingEnd * progress,
                measuredHeight - paddingBottom + mStarStrokeWidth), mStarPaint)
        mStarPaint.xfermode = null
    }

    private fun Canvas.drawStars(paint: Paint) {
        for (i in 0 until mCount) {
            if (i == 0) {
                translate(paddingStart.toFloat(), paddingTop.toFloat())
            } else {
                translate(mStarSize.toFloat(), 0F)
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

    /**
     *
     * 设置星星数量
     *
     * @author Jero
     *
     */
    fun count(count: Int): PercentStarView {
        this.mCount = count
        return this
    }

    /**
     * 获取星星数量
     *
     * @author Jero
     */
    fun count() = mCount

    /**
     *
     * 设置描边是否覆盖进度星星
     *
     * @author Jero
     *
     */
    fun strokeOverride(isOverride: Boolean): PercentStarView {
        this.mIsStrokeOverride = isOverride
        return this
    }

    /**
     * 获取描边是否覆盖进度星星的状态
     *
     * @return true 表示覆盖进度，false 表示不覆盖
     *
     * @author Jero
     */
    fun strokeOverride() = mIsStrokeOverride

    /**
     *
     * 设置星星大小
     *
     * @author Jero
     *
     */
    fun starSize(size: Int): PercentStarView {
        this.mStarSize = size
        return this
    }

    /**
     * 获取星星大小
     *
     * @author Jero
     */
    fun starSize() = mStarSize

    /**
     *
     * 设置形状饱满度
     *
     * 取值范围：0.2~0.8
     *
     * @author Jero
     *
     */
    fun bmi(@FloatRange(from = MIN_BMI.toDouble(), to = MAX_BMI.toDouble()) bmi: Float): PercentStarView {
        this.mBmi = bmi
        return this
    }

    /**
     * 获取形状饱满度
     *
     * 取值范围：0.2~0.8
     *
     * @author Jero
     */
    fun bmi() = mBmi

    /**
     *
     * 设置描边笔画颜色
     *
     * @author Jero
     *
     */
    fun starStrokeColor(color: Int): PercentStarView {
        this.mStarStrokeColor = color
        return this
    }

    /**
     *
     * 设置描边笔画宽度
     *
     * @author Jero
     *
     */
    fun strokeWidth(width: Float): PercentStarView {
        this.mStarStrokeWidth = width
        return this
    }

    /**
     * 获取描边笔画宽度
     *
     * @author Jero
     */
    fun strokeWidth() = mStarStrokeWidth

    /**
     *
     * 设置背景颜色
     *
     * @author Jero
     *
     */
    fun starBackColor(color: Int): PercentStarView {
        this.mStarBackColor = color
        return this
    }

    /**
     *
     * 设置进度颜色
     *
     * @author Jero
     *
     */
    fun starProgressColor(color: Int): PercentStarView {
        this.mStarProgressColor = color
        return this
    }

    /**
     *
     * 设置最大值
     *
     * @author Jero
     *
     */
    fun max(max: Int): PercentStarView {
        this.mMax = max
        return this
    }

    /**
     * 获取最大值
     *
     * @author Jero
     */
    fun max() = mMax

    /**
     *
     * 设置进度
     *
     * @author Jero
     *
     */
    fun progress(progress: Int): PercentStarView {
        this.mProgress = progress
        return this
    }

    /**
     * 获取进度
     *
     * @author Jero
     */
    fun progress() = mProgress

}
