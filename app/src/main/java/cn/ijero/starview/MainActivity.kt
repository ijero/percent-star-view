package cn.ijero.starview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onAttachedToWindow() {
        setListeners()
        load()
    }

    private fun load() {
        maxSeekBar.progress = mainPercentStarView.max()
        progressSeekBar.progress = mainPercentStarView.progress()
        countSeekBar.progress = mainPercentStarView.count() - 1
        strokeWidthSeekBar.progress = mainPercentStarView.strokeWidth().toInt()
        bmiSeekBar.progress = ((mainPercentStarView.bmi() - 0.2f) * 100).toInt()
        sizeSeekBar.progress = mainPercentStarView.starSize()

        maxTextView.text = mainPercentStarView.max().toString()
        progressTextView.text = mainPercentStarView.progress().toString()
        countTextView.text = mainPercentStarView.count().toString()
        strokeWidthTextView.text = mainPercentStarView.strokeWidth().toString()
        bmiTextView.text = mainPercentStarView.bmi().toString()
        sizeTextView.text = mainPercentStarView.starSize().toString()

        overrideToggleButton.isChecked = mainPercentStarView.strokeOverride()
    }

    private fun setListeners() {
        maxSeekBar.setOnSeekBarChangeListener(this)
        progressSeekBar.setOnSeekBarChangeListener(this)
        countSeekBar.setOnSeekBarChangeListener(this)
        strokeWidthSeekBar.setOnSeekBarChangeListener(this)
        bmiSeekBar.setOnSeekBarChangeListener(this)
        sizeSeekBar.setOnSeekBarChangeListener(this)
        overrideToggleButton.setOnCheckedChangeListener(this)

        strokeColorButton.setOnClickListener(this)
        backColorButton.setOnClickListener(this)
        progressColorButton.setOnClickListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.maxSeekBar -> {
                mainPercentStarView.max(progress)
                maxTextView.text = progress.toString()
            }

            R.id.progressSeekBar -> {
                mainPercentStarView.progress(progress)
                progressTextView.text = progress.toString()
            }

            R.id.countSeekBar -> {
                mainPercentStarView.count(progress + 1)
                countTextView.text = (progress + 1).toString()
            }

            R.id.strokeWidthSeekBar -> {
                mainPercentStarView.strokeWidth(progress.toFloat())
                strokeWidthTextView.text = progress.toString()
            }

            R.id.bmiSeekBar -> {
                val bmi = progress.toFloat() / bmiSeekBar.max
                mainPercentStarView.bmi(bmi)
                bmiTextView.text = bmi.toString()
            }

            R.id.sizeSeekBar -> {
                mainPercentStarView.starSize(progress)
                sizeTextView.text = progress.toString()
            }

            else -> {
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        mainPercentStarView.strokeOverride(isChecked)
    }

    override fun onClick(v: View) {
        val random = Random()
        val r = random.nextInt(254)
        val g = random.nextInt(254)
        val b = random.nextInt(254)
        when (v.id) {
            R.id.strokeColorButton -> mainPercentStarView.starStrokeColor(Color.rgb(r, g, b))

            R.id.backColorButton -> mainPercentStarView.starBackColor(Color.rgb(r, g, b))

            R.id.progressColorButton -> mainPercentStarView.starProgressColor(Color.rgb(r, g, b))

            else -> {
            }
        }
    }
}
