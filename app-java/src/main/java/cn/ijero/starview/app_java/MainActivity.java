package cn.ijero.starview.app_java;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

import cn.ijero.psv.PercentStarView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private PercentStarView percentStarView;
    private SeekBar maxSeekBar;
    private SeekBar progressSeekBar;
    private SeekBar countSeekBar;
    private SeekBar strokeWidthSeekBar;
    private SeekBar bmiSeekBar;
    private SeekBar sizeSeekBar;
    private SeekBar spacingSeekBar;
    private TextView maxTextView;
    private TextView progressTextView;
    private TextView countTextView;
    private TextView strokeWidthTextView;
    private TextView bmiTextView;
    private TextView sizeTextView;
    private TextView spacingTextView;
    private ToggleButton overrideToggleButton;
    private Button strokeColorButton;
    private Button backColorButton;
    private Button progressColorButton;

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
        spacingTextView = findViewById(R.id.spacingTextView);


        overrideToggleButton = findViewById(R.id.overrideToggleButton);
        strokeColorButton = findViewById(R.id.strokeColorButton);
        backColorButton = findViewById(R.id.backColorButton);
        progressColorButton = findViewById(R.id.progressColorButton);
        spacingSeekBar = findViewById(R.id.spacingSeekBar);

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
        load();
    }

    private void load() {
        maxSeekBar.setProgress(percentStarView.max());
        progressSeekBar.setProgress(percentStarView.progress());
        countSeekBar.setProgress(percentStarView.count() - 1);
        strokeWidthSeekBar.setProgress((int) percentStarView.strokeWidth());
        bmiSeekBar.setProgress((int) ((percentStarView.bmi() - 0.2F) * 100));
        sizeSeekBar.setProgress(percentStarView.starSize());
        spacingSeekBar.setProgress((int) percentStarView.spacing());

        maxTextView.setText(String.valueOf(percentStarView.max()));
        progressTextView.setText(String.valueOf(percentStarView.progress()));
        countTextView.setText(String.valueOf(percentStarView.count()));
        strokeWidthTextView.setText(String.valueOf(percentStarView.strokeWidth()));
        bmiTextView.setText(String.valueOf(percentStarView.bmi()));
        sizeTextView.setText(String.valueOf(percentStarView.starSize()));
        spacingTextView.setText(String.valueOf(percentStarView.spacing()));

        overrideToggleButton.setChecked(percentStarView.strokeOverride());
    }

    private void setListeners() {
        maxSeekBar.setOnSeekBarChangeListener(this);
        progressSeekBar.setOnSeekBarChangeListener(this);
        countSeekBar.setOnSeekBarChangeListener(this);
        strokeWidthSeekBar.setOnSeekBarChangeListener(this);
        bmiSeekBar.setOnSeekBarChangeListener(this);
        sizeSeekBar.setOnSeekBarChangeListener(this);
        spacingSeekBar.setOnSeekBarChangeListener(this);
        overrideToggleButton.setOnCheckedChangeListener(this);

        strokeColorButton.setOnClickListener(this);
        backColorButton.setOnClickListener(this);
        progressColorButton.setOnClickListener(this);
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
            case R.id.spacingSeekBar:
                percentStarView.spacing(progress);
                spacingTextView.setText(String.valueOf(progress));
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        percentStarView.strokeOverride(isChecked);
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        int r = random.nextInt(254);
        int g = random.nextInt(254);
        int b = random.nextInt(254);
        switch (v.getId()) {
            case R.id.strokeColorButton:
                percentStarView.starStrokeColor(Color.rgb(r, g, b));
                break;

            case R.id.backColorButton:
                percentStarView.starBackColor(Color.rgb(r, g, b));
                break;

            case R.id.progressColorButton:
                percentStarView.starProgressColor(Color.rgb(r, g, b));
                break;

            default:
                break;
        }
    }
}
