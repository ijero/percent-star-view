package cn.ijero.starview.app_java;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;

import cn.ijero.psv.PercentStarView;

public class MainActivity extends AppCompatActivity {
    private PercentStarView percentStarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        percentStarView = findViewById(R.id.mainPercentStarView);
        percentStarView.max(200)
                .progress(120)
                .bmi(0.3F)
                .count(4)
                .starBackColor(Color.WHITE)
                .starProgressColor(Color.RED)
                .starStrokeColor(Color.parseColor("#FF666666"))
                .starSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()))
                .strokeWidth(1)
                .strokeOverride(true);
    }


}
