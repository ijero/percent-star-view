package cn.ijero.psv

import android.content.Context
import android.graphics.*
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
    private var mCount = 5
    private var mMax = 100
    private var mProgress = 100
    private var mBackColor = Color.parseColor("#FFF0F0F0")
    private var mStarBackColor = Color.parseColor("#FFFFFFFF")
    private var mProgressColor = Color.YELLOW
    private var mStrokeWidth = 1F
    private var mStrokeColor = Color.parseColor("#FF666666")
    private var mOuterRadius = 0F
    private var mInnerRadius = 0F

    private var mStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = mStrokeColor
        strokeWidth = mStrokeWidth
    }

    private var mStarBackPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = mStarBackColor
        strokeWidth = mStrokeWidth
    }

    private var mBackPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = mBackColor
    }

    private var mProgressPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = mProgressColor
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private var mStarSize = 0


    init {
        applyStyle(attrs)
    }

    private fun applyStyle(attrs: AttributeSet?) {
        attrs ?: return

        mStarSize = dip(50)
        mOuterRadius = (mStarSize / 2).toFloat()
        mInnerRadius = (mStarSize / 5).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getWidthSize(widthMeasureSpec), getHeightSize(heightMeasureSpec))
    }

    private fun getWidthSize(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return mStarSize * mCount + paddingLeft + paddingRight
        }

        return size
    }

    private fun getHeightSize(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)

        if (mode == MeasureSpec.AT_MOST) {
            return mStarSize + paddingBottom + paddingTop
        }

        return size
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBack()

        canvas.translate(mStarSize * 0.025f, mStarSize * 0.05f) // 减少上下和左右的间距差

        canvas.save()
        canvas.drawStars(mStarBackPaint)
        canvas.restore()
        canvas.drawStars(mStrokePaint)
        canvas.restore()
        canvas.drawProgress()
    }


    private fun Canvas.drawProgress() {
//        drawRect(getCanDrawingRect(), mStarBackPaint.apply {
//            color = mProgressColor
////            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
//        })
    }

    private fun Canvas.drawBack() {
        drawRect(getCanDrawingRect(), mBackPaint)
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
    }

    private fun Canvas.drawStar(paint: Paint) {
        val path = Path().apply {
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
        }

        drawPath(path, paint)

    }

    private fun getCanDrawingRect(): RectF {
        return RectF(paddingStart.toFloat(), paddingTop.toFloat(),
                (measuredWidth - paddingEnd).toFloat(),
                (measuredHeight - paddingBottom).toFloat())
    }

}
