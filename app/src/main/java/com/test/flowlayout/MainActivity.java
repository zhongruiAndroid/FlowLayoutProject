package com.test.flowlayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.flowview.FlowLayout;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private View btReAddView;
    private FlowLayout flView;
    private AppCompatSeekBar sbHGap;
    private AppCompatSeekBar sbVGap;
    private RadioGroup rg;
    private RadioGroup rg2;
    private RadioButton rbLeft;
    private RadioButton rbRight;
    private RadioButton rbCenter;
    private RadioButton rbAlign;
    private AppCompatSeekBar sbLeft;
    private AppCompatSeekBar sbTop;
    private AppCompatSeekBar sbRight;
    private AppCompatSeekBar sbBottom;
    private AppCompatSeekBar sbMaxNum;
    private AppCompatSeekBar sbMaxLine;


    private TextView tvMaxNum;
    private TextView tvMaxLine;

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
                switch (checkedId) {
                    case R.id.rbLeft:
                        flView.setGravity(FlowLayout.gravity_left);
                        break;
                    case R.id.rbRight:
                        flView.setGravity(FlowLayout.gravity_right);
                        break;
                    case R.id.rbCenter:
                        flView.setGravity(FlowLayout.gravity_center);
                        break;
                    case R.id.rbAlign:
                        flView.setGravity(FlowLayout.gravity_left_right_align);
                        break;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbTop:
                        flView.setGravityVertical(FlowLayout.gravity_top);
                        break;
                    case R.id.rbBottom:
                        flView.setGravityVertical(FlowLayout.gravity_bottom);
                        break;
                    case R.id.rbCenterVertical:
                        flView.setGravityVertical(FlowLayout.gravity_center);
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

        sbMaxNum.setOnSeekBarChangeListener(this);
        sbMaxLine.setOnSeekBarChangeListener(this);

    }

    private void initView() {
        btReAddView = findViewById(R.id.btReAddView);
        sbHGap = findViewById(R.id.sbHGap);
        sbVGap = findViewById(R.id.sbVGap);
        rg = findViewById(R.id.rg);
        rg2 = findViewById(R.id.rg2);
        rbLeft = findViewById(R.id.rbLeft);
        rbRight = findViewById(R.id.rbRight);
        rbCenter = findViewById(R.id.rbCenter);
        rbAlign = findViewById(R.id.rbAlign);
        sbLeft = findViewById(R.id.sbLeft);
        sbTop = findViewById(R.id.sbTop);
        sbRight = findViewById(R.id.sbRight);
        sbBottom = findViewById(R.id.sbBottom);
        sbMaxNum = findViewById(R.id.sbMaxNum);
        sbMaxLine = findViewById(R.id.sbMaxLine);
        tvMaxNum = findViewById(R.id.tvMaxNum);
        tvMaxLine = findViewById(R.id.tvMaxLine);

        addView();

    }

    private String[] str = {
            "android",
            "ios",
            "java",
            "php",
            "swift",
            "python",
            "C#",
            "C++",
            ".Net",
            "SQLServer",
            "Oracle",
            "android studio",
            "eclipse",
            "ide",
            "sublime"
    };

    private void addView() {
        flView.removeAllViews();
        int size=str.length;
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(this);
            textView.setBackgroundResource(R.drawable.viewbg);
            textView.setText(str[i]);
            if(i==9){
                textView.setGravity(Gravity.CENTER);
                flView.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (Resources.getSystem().getDisplayMetrics().density*38)));
            }else{
                flView.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (Resources.getSystem().getDisplayMetrics().density*24)));
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int left = flView.getPaddingLeft();
        int top = flView.getPaddingTop();
        int right = flView.getPaddingRight();
        int bottom = flView.getPaddingBottom();
        switch (seekBar.getId()) {
            case R.id.sbHGap:
                flView.setHGap(dp2px(progress));
                break;
            case R.id.sbVGap:
                flView.setVGap(dp2px(progress));
                break;
            case R.id.sbLeft:
                flView.setPadding(dp2px(progress), top, right, bottom);
                break;
            case R.id.sbTop:
                flView.setPadding(left, dp2px(progress), right, bottom);
                break;
            case R.id.sbRight:
                flView.setPadding(left, top, dp2px(progress), bottom);
                break;
            case R.id.sbBottom:
                flView.setPadding(left, top, right, dp2px(progress));
                break;
            case R.id.sbMaxNum:
                flView.setMaxNum(progress);
                tvMaxNum.setText("显示最大数量" + progress);
                addView();
                break;
            case R.id.sbMaxLine:
                flView.setMaxLine(progress);
                tvMaxLine.setText("显示最大行数" + progress);
                addView();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private int dp2px(int dp) {
        return dp;//(int) (getResources().getDisplayMetrics().density*dp);
    }
}
