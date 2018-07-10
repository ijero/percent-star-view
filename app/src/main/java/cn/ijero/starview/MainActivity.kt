package cn.ijero.starview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPercentStarView.max(200)
                .progress(120)
                .bmi(0.3f)
                .count(4)
                .starBackColor(Color.WHITE)
                .starProgressColor(Color.RED)
                .starStrokeColor(Color.parseColor("#FF666666"))
                .starSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, resources.displayMetrics).toInt())
                .strokeWidth(1f)
                .strokeOverride(true)
    }
}
