package com.github.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/***
 *   created by zhongrui on 2019/5/27
 */
public class FlowLayout extends ViewGroup{

    private float bothGap;
    private float vGap;
    private float hGap;

    public FlowLayout(Context context) {
        super(context);
        init(null);
    }
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        bothGap = typedArray.getDimension(R.styleable.FlowLayout_bothGap, 0);

        vGap = typedArray.getDimension(R.styleable.FlowLayout_vGap, 0);
        hGap = typedArray.getDimension(R.styleable.FlowLayout_hGap, 0);

        typedArray.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int childCount=getChildCount();
        if(childCount<=0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChildWithMargins(childView,widthSize,0,heightSize,0);
            int childViewMeasuredWidth = childView.getMeasuredWidth();
            int childViewMeasuredHeight = childView.getMeasuredHeight();

        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public float getBothGap() {
        return bothGap;
    }

    public FlowLayout setBothGap(float bothGap) {
        this.bothGap = bothGap;
        return this;
    }

    public float getVGap() {
        if(getBothGap()>0){
            return getBothGap();
        }
        return vGap;
    }

    public FlowLayout setVGap(float vGap) {
        this.vGap = vGap;
        return this;
    }

    public float getHGap() {
        if(getBothGap()>0){
            return getBothGap();
        }
        return hGap;
    }

    public FlowLayout setHGap(float hGap) {
        this.hGap = hGap;
        return this;
    }
}
