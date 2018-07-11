package cn.ijero.starview.app_java;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.ijero.psv.PercentStarView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private PercentStarView percentStarView;
    private SeekBar maxSeekBar;
    private SeekBar progressSeekBar;
    private SeekBar countSeekBar;
    private SeekBar strokeWidthSeekBar;
    private SeekBar bmiSeekBar;
    private SeekBar sizeSeekBar;
    private TextView maxTextView;
    private TextView progressTextView;
    private TextView countTextView;
    private TextView strokeWidthTextView;
    private TextView bmiTextView;
    private TextView sizeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        percentStarView = findViewById(R.id.mainPercentStarView);

        maxSeekBar = findViewById(R.id.maxSeekBar);
        progressSeekBar = findViewById(R.id.progressSeekBar);
        countSeekBar = findViewById(R.id.countSeekBar);
        strokeWidthSeekBar = findViewById(R.id.strokeWidthSeekBar);
        bmiSeekBar = findViewById(R.id.bmiSeekBar);
        sizeSeekBar = findViewById(R.id.sizeSeekBar);

        maxTextView = findViewById(R.id.maxTextView);
        progressTextView = findViewById(R.id.progressTextView);
        countTextView = findViewById(R.id.countTextView);
        strokeWidthTextView = findViewById(R.id.strokeWidthTextView);
        bmiTextView = findViewById(R.id.bmiTextView);
        sizeTextView = findViewById(R.id.sizeTextView);

//        percentStarView.max(200)
//                .progress(120)
//                .bmi(0.1F)
//                .count(4)
//                .starBackColor(Color.WHITE)
//                .starProgressColor(Color.RED)
//                .starStrokeColor(Color.parseColor("#FF666666"))
//                .starSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()))
//                .strokeWidth(1)
//                .strokeOverride(true);

    }

    @Override
    public void onAttachedToWindow() {
        setListeners();
        loadTexts();
    }

    private void loadTexts() {
        maxSeekBar.setProgress(percentStarView.max());
        progressSeekBar.setProgress(percentStarView.progress());
        countSeekBar.setProgress(percentStarView.count() - 1);
        strokeWidthSeekBar.setProgress((int) percentStarView.strokeWidth());
        bmiSeekBar.setProgress((int) percentStarView.bmi() * 100);
        sizeSeekBar.setProgress(percentStarView.starSize());

        maxTextView.setText(String.valueOf(percentStarView.max()));
        progressTextView.setText(String.valueOf(percentStarView.progress()));
        countTextView.setText(String.valueOf(percentStarView.count()));
        strokeWidthTextView.setText(String.valueOf(percentStarView.strokeWidth()));
        bmiTextView.setText(String.valueOf(percentStarView.bmi()));
        sizeTextView.setText(String.valueOf(percentStarView.starSize()));
    }

    private void setListeners() {
        maxSeekBar.setOnSeekBarChangeListener(this);
        progressSeekBar.setOnSeekBarChangeListener(this);
        countSeekBar.setOnSeekBarChangeListener(this);
        strokeWidthSeekBar.setOnSeekBarChangeListener(this);
        bmiSeekBar.setOnSeekBarChangeListener(this);
        sizeSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.maxSeekBar:
                percentStarView.max(progress);
                maxTextView.setText(String.valueOf(progress));
                break;

            case R.id.progressSeekBar:
                percentStarView.progress(progress);
                progressTextView.setText(String.valueOf(progress));
                break;

            case R.id.countSeekBar:
                percentStarView.count(progress + 1);
                countTextView.setText(String.valueOf(progress + 1));
                break;

            case R.id.strokeWidthSeekBar:
                percentStarView.strokeWidth(progress);
                strokeWidthTextView.setText(String.valueOf(progress));
                break;

            case R.id.bmiSeekBar:
                float bmi = (float) progress / bmiSeekBar.getMax();
                percentStarView.bmi(bmi);
                bmiTextView.setText(String.valueOf(bmi));
                break;

            case R.id.sizeSeekBar:
                percentStarView.starSize(progress);
                sizeTextView.setText(String.valueOf(progress));
                break;

            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
