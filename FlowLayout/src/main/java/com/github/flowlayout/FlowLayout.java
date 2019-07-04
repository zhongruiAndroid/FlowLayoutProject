package com.github.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/***
 *   created by zhongrui on 2019/5/27
 */
public class FlowLayout extends ViewGroup {
    private List<Point> pointList = new ArrayList<>();
//    private SparseArray<Point> offsetX = new SparseArray<>();

    private int vGap;
    private int hGap;
    public static final int gravity_left=0;
    public static final int gravity_center_horizontal=1;
    public static final int gravity_right=2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({gravity_left,gravity_center_horizontal,gravity_right})
    public @interface gravity_type{}
    private int gravity=gravity_left;

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
        float bothGap = typedArray.getDimension(R.styleable.FlowLayout_bothGap, 0);

        vGap = (int) typedArray.getDimension(R.styleable.FlowLayout_vGap, bothGap);
        hGap = (int) typedArray.getDimension(R.styleable.FlowLayout_hGap, bothGap);
        gravity =typedArray.getInt(R.styleable.FlowLayout_gravity, gravity_left);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pointList.clear();
//        offsetX.clear();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int resultWidth = 0;
        int resultHeight = 0;

        int horizontalWidth = 0;
        int verticalHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidthMeasureSpec = getChildMeasureSpec(widthSize, lp.leftMargin + lp.rightMargin, lp.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightSize, lp.topMargin + lp.bottomMargin, lp.height);

            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int childViewMeasuredWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childViewMeasuredHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (horizontalWidth + childViewMeasuredWidth > widthSize -getPaddingLeft()- getPaddingRight()) {

                int temp = widthSize - getPaddingLeft() - getPaddingRight() - horizontalWidth;
//                offsetX.put(i,new Point(temp,0));

                resultWidth = Math.max(resultWidth, horizontalWidth);
                //如果换行，从第下一行开始计算每行宽度
                horizontalWidth = childViewMeasuredWidth + getHGap();

                resultHeight += verticalHeight;
                //如果换行,保存下一行的起始高度
                verticalHeight = childViewMeasuredHeight + getVGap();

                //记录view布局位置的left和top坐标
                pointList.add(new Point(0, resultHeight));
            } else {
                pointList.add(new Point(horizontalWidth, resultHeight));
                //如果不换行，累加每行的view宽度
                horizontalWidth = horizontalWidth + childViewMeasuredWidth + getHGap();
                //如果不换行，计算每行最高view的高度
                verticalHeight = Math.max(verticalHeight, childViewMeasuredHeight + getVGap());
            }

            if (i == (childCount - 1)) {
                resultWidth = Math.max(resultWidth, horizontalWidth) - getHGap();
                resultHeight = resultHeight + verticalHeight - getVGap();
            }
        }
        resultWidth = resultWidth + getPaddingLeft() + getPaddingRight();
        resultHeight = resultHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : resultWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : resultHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int beforeOffsetWidth=0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            Point point = pointList.get(i);


            float offset=gravity/2f;
            if (gravity<0||gravity>2){
                offset=0;
            }
//            Point pointX = offsetX.get(i);
//            if (pointX != null) {
//                beforeOffsetWidth= (int) (pointX.x*offset);
//            }
            childView.layout(
                    point.x + lp.leftMargin + getPaddingLeft()+beforeOffsetWidth,
                    point.y + lp.topMargin  + getPaddingTop(),
                    point.x + lp.leftMargin + childWidth + getPaddingLeft()+beforeOffsetWidth,
                    point.y + lp.topMargin  + childHeight + getPaddingTop());



        }
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setBothGap(int bothGap) {
        setHGap(bothGap);
        setVGap(bothGap);
    }

    public int getVGap() {
        return vGap;
    }

    public FlowLayout setVGap(int vGap) {
        if (vGap < 0) {
            vGap = 0;
        }
        this.vGap = vGap;
        return this;
    }

    public int getHGap() {
        return hGap;
    }

    public FlowLayout setHGap(int hGap) {
        if (hGap < 0) {
            hGap = 0;
        }
        this.hGap = hGap;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(@gravity_type int gravity) {
        if(gravity!=gravity_left&&gravity!=gravity_center_horizontal&&gravity!=gravity_right){
            gravity=gravity_left;
        }
        this.gravity = gravity;
    }
}
