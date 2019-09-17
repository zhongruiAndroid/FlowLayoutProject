package com.test.flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.github.flowview.FlowLayout;
import com.github.flowview.FlowLayout;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private FlowLayout flView;
    private AppCompatSeekBar sbHGap;
    private AppCompatSeekBar sbVGap;
    private RadioGroup rg;
    private RadioButton rbLeft;
    private RadioButton rbRight;
    private RadioButton rbCenter;
    private AppCompatSeekBar sbLeft;
    private AppCompatSeekBar sbTop;
    private AppCompatSeekBar sbRight;
    private AppCompatSeekBar sbBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flView = findViewById(R.id.flView);
        initView();
//        TextView textView = new TextView(this);
//        textView.setText("TextView");
//        flView.addView(textView);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbLeft:
                        flView.setGravity(FlowLayout.gravity_left);
                    break;
                    case R.id.rbRight:
                        flView.setGravity(FlowLayout.gravity_right);
                    break;
                    case R.id.rbCenter:
                        flView.setGravity(FlowLayout.gravity_center_horizontal);
                    break;
                }
            }
        });

        sbHGap.setMax(50);
        sbVGap.setMax(50);
        sbLeft.setMax(50);
        sbTop.setMax(50);
        sbRight.setMax(50);
        sbBottom.setMax(50);

        sbHGap.setOnSeekBarChangeListener(this);
        sbVGap.setOnSeekBarChangeListener(this);
        sbLeft.setOnSeekBarChangeListener(this);
        sbTop.setOnSeekBarChangeListener(this);
        sbRight.setOnSeekBarChangeListener(this);
        sbBottom.setOnSeekBarChangeListener(this);

    }

    private void initView() {
        sbHGap = findViewById(R.id.sbHGap);
        sbVGap = findViewById(R.id.sbVGap);
        rg = findViewById(R.id.rg);
        rbLeft = findViewById(R.id.rbLeft);
        rbRight = findViewById(R.id.rbRight);
        rbCenter = findViewById(R.id.rbCenter);
        sbLeft = findViewById(R.id.sbLeft);
        sbTop = findViewById(R.id.sbTop);
        sbRight = findViewById(R.id.sbRight);
        sbBottom = findViewById(R.id.sbBottom);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int left=flView.getPaddingLeft();
        int top=flView.getPaddingTop();
        int right=flView.getPaddingRight();
        int bottom=flView.getPaddingBottom();
        switch (seekBar.getId()){
            case R.id.sbHGap:
                flView.setHGap(dp2px(progress));
            break;
            case R.id.sbVGap:
                flView.setVGap(dp2px(progress));
            break;
            case R.id.sbLeft:
                flView.setPadding(dp2px(progress),top,right,bottom);
            break;
            case R.id.sbTop:
                flView.setPadding(left,dp2px(progress),right,bottom);
            break;
            case R.id.sbRight:
                flView.setPadding(left,top,dp2px(progress),bottom);
            break;
            case R.id.sbBottom:
                flView.setPadding(left,top,right,dp2px(progress));
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private int dp2px(int dp){
        return dp;//(int) (getResources().getDisplayMetrics().density*dp);
    }
}
