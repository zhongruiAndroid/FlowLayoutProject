package com.github.flowview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/***
 *   created by zhongrui on 2019/5/27
 */
public class FlowLayoutNew extends ViewGroup {
    private List<List<View>>lineView=new ArrayList<>();

    //每行第一个view的left坐标
    private List<Integer>eachLineLeft=new ArrayList<>();

    //每行最大的view高度
    private List<Integer>eachLineHeight=new ArrayList<>();

    private int vGap;
    private int hGap;
    public static final int gravity_left=0;
    public static final int gravity_center_horizontal=1;
    public static final int gravity_right=2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({gravity_left,gravity_center_horizontal,gravity_right})
    private  @interface gravity_type{}
    private int gravity=gravity_left;

    public FlowLayoutNew(Context context) {
        super(context);
        init(null);
    }

    public FlowLayoutNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlowLayoutNew(Context context, AttributeSet attrs, int defStyleAttr) {
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
        eachLineLeft.clear();
        eachLineHeight.clear();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        //子view内容宽度
        int resultWidth = 0;
        //子view内容高度
        int resultHeight = 0;

        //每行宽度
        int horizontalWidth = 0;
        //每行高度
        int verticalHeight = 0;

        int childCount = getChildCount();

        int lineWidth=widthSize -getPaddingLeft()- getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            measureChildWithMargins(childView,widthSize,0,heightSize,0);

            LayoutParams lp = (LayoutParams) childView.getLayoutParams();

            //子view所占的宽度
            int childViewMeasuredWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子view所占的高度
            int childViewMeasuredHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //换行
            if (horizontalWidth + childViewMeasuredWidth > lineWidth) {
                //记录每行的left距离
                int left = (int) ((lineWidth - (horizontalWidth - getHGap()))*getHorizontalOffsetScale());
                eachLineLeft.add(left);

                //获取最大宽度的一行
                resultWidth = Math.max(resultWidth, horizontalWidth);
                //如果换行，从第下一行开始计算每行宽度
                horizontalWidth = childViewMeasuredWidth + getHGap();

                resultHeight += verticalHeight;
                //记录换行时上行最大的view高度
                eachLineHeight.add(verticalHeight);

                //换行后保存下一行的view高度
                verticalHeight = childViewMeasuredHeight + getVGap();

            } else {
                //如果不换行，累加每行的view宽度以及view间隙
                horizontalWidth = horizontalWidth + childViewMeasuredWidth + getHGap();
                //如果不换行，计算每行最高view的高度
                verticalHeight = Math.max(verticalHeight, childViewMeasuredHeight + getVGap());
            }
            if (i == (childCount - 1)) {

                //记录每行的left距离
                int left = (int) ((lineWidth - (horizontalWidth - getHGap()))*getHorizontalOffsetScale());
                eachLineLeft.add(left);
                //记录最后一行最大的view高度
                eachLineHeight.add(verticalHeight);

                //最后需要减去多余的一个间隙
                resultWidth = Math.max(resultWidth, horizontalWidth) - getHGap();
                resultHeight = resultHeight + verticalHeight - getVGap();

            }
        }

        resultWidth  = resultWidth + getPaddingLeft() + getPaddingRight();
        resultHeight = resultHeight + getPaddingTop() + getPaddingBottom();
        Log.i("=============","getPaddingTop()=="+getPaddingTop()+"===getPaddingBottom()=="+getPaddingBottom()+"============resultHeight="+resultHeight);
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : resultWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : resultHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        lineView.clear();

        //每行宽度
        int horizontalWidth = 0;
        //每行高度
        int verticalHeight = 0;

        List<View>eachLineView=new ArrayList<>();

        int lineWidth=getWidth()-getPaddingLeft()-getPaddingRight();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            LayoutParams lp = (LayoutParams) childView.getLayoutParams();

            //子view所占的宽度
            int childViewMeasuredWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子view所占的高度
            int childViewMeasuredHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (horizontalWidth + childViewMeasuredWidth >lineWidth) {
                //换行
                lineView.add(eachLineView);
                //如果换行，从第下一行开始计算每行宽度
                horizontalWidth = childViewMeasuredWidth + getHGap();

                //如果换行,保存下一行的view高度
                verticalHeight = childViewMeasuredHeight + getVGap();
                eachLineView=new ArrayList<>();
                eachLineView.add(childView);
            } else {
                eachLineView.add(childView);
                //如果不换行，累加每行的view宽度以及view间隙
                horizontalWidth = horizontalWidth + childViewMeasuredWidth + getHGap();
                //如果不换行，计算每行最高view的高度
                verticalHeight = Math.max(verticalHeight, childViewMeasuredHeight + getVGap());
            }
            if (i == (childCount - 1)) {
                lineView.add(eachLineView);
            }

        }
        int mPaddingLeft=getPaddingLeft();
        int mPaddingTop=getPaddingTop();
        int leftLocation=0;
        int topLocation=0;
        int lineCount=lineView.size();
        for (int i = 0; i < lineCount; i++) {
            leftLocation=eachLineLeft.get(i);
            List<View> lineViews = lineView.get(i);
            int size = lineViews.size();
            for (int j = 0; j < size; j++) {
                View childView = lineViews.get(j);
                if (childView.getVisibility() == View.GONE) {
                    continue;
                }
                int childWidth=childView.getMeasuredWidth();
                int childHeight=childView.getMeasuredHeight();

                LayoutParams lp = (LayoutParams) childView.getLayoutParams();
                childView.layout(
                        mPaddingLeft+leftLocation+ + lp.leftMargin,
                        mPaddingTop+topLocation + lp.topMargin,
                        mPaddingLeft+leftLocation+ + lp.leftMargin+ childWidth ,
                        mPaddingTop+topLocation + lp.topMargin + childHeight);

                leftLocation+=childWidth+lp.leftMargin+lp.rightMargin+getHGap();
            }
            topLocation+=eachLineHeight.get(i);
        }


        eachLineView=null;
    }

    private float getHorizontalOffsetScale(){
        return getGravity()/2f;
    }
    private float getVerticalOffsetScale(){
        return getGravity()/2f;
    }
    public void setBothGap(int bothGap) {
        setHGap(bothGap);
        setVGap(bothGap);
    }

    public int getVGap() {
        return vGap;
    }

    public FlowLayoutNew setVGap(int vGap) {
        if (vGap < 0) {
            vGap = 0;
        }
        if(this.vGap!=vGap){
            this.vGap = vGap;
            requestLayout();
        }
        return this;
    }

    public int getHGap() {
        return hGap;
    }

    public FlowLayoutNew setHGap(int hGap) {
        if (hGap < 0) {
            hGap = 0;
        }
        if(this.hGap != hGap){
            this.hGap = hGap;
            requestLayout();
        }
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public FlowLayoutNew setGravity(@gravity_type int gravity) {
        if(gravity!=gravity_left&&gravity!=gravity_center_horizontal&&gravity!=gravity_right){
            gravity=gravity_left;
        }
        if(this.gravity != gravity){
            this.gravity = gravity;
            requestLayout();
        }
        return this;
    }

    @Override
    public FlowLayoutNew.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayoutNew.LayoutParams(getContext(), attrs);
    }
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FlowLayoutNew.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof FlowLayoutNew.LayoutParams) {
            return new FlowLayoutNew.LayoutParams((FlowLayoutNew.LayoutParams) lp);
        } else if (lp instanceof MarginLayoutParams) {
            return new FlowLayoutNew.LayoutParams((MarginLayoutParams) lp);
        }
        return new FlowLayoutNew.LayoutParams(lp);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
        }
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }
        public LayoutParams(@NonNull MarginLayoutParams source) {
            super(source);
        }
        public LayoutParams(@NonNull FlowLayoutNew.LayoutParams source) {
            super(source);
        }
    }
}
