package com.github.flowview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    /*每行的view列表*/
    private List<List<View>> lineView = new ArrayList<>();

    //每行第一个view的left坐标
    private List<Integer> eachLineLeft = new ArrayList<>();

    //每行最大的view高度(不包括垂直间距)
    private List<Integer> eachLineHeight = new ArrayList<>();

    //每行剩余宽度
    private List<Integer> eachLineFreeWidth = new ArrayList<>();

    private int vGap;
    private int hGap;
    public static final int gravity_left = 0;
    public static final int gravity_center = 1;
    public static final int gravity_right = 2;
    public static final int gravity_left_right_align = 3;
    public static final int gravity_left_right_align_ignore_last_line = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({gravity_left, gravity_center, gravity_right, gravity_left_right_align,gravity_left_right_align_ignore_last_line})
    private @interface gravity_type {
    }

    private int gravity = gravity_left;


    public static final int gravity_top = 0;
    public static final int gravity_bottom = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({gravity_top, gravity_center, gravity_bottom})
    private @interface gravity_type_vertical {
    }

    private int gravityVertical = gravity_top;

    private int maxNum;
    private int maxLine;

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
        gravity = typedArray.getInt(R.styleable.FlowLayout_gravity, gravity_left);
        gravityVertical = typedArray.getInt(R.styleable.FlowLayout_gravity_vertical, gravity_top);

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        eachLineFreeWidth.clear();
        lineView.clear();
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

        //每行子view总宽度
        int eachViewTotalWidth = 0;
        //每行宽度
        int horizontalWidth = 0;
        //每行高度
        int verticalHeight = 0;

        int childCount = getChildCount();

        /*保存每行view列表*/
        List<View> eachLineView = new ArrayList<>();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int lineWidth = widthSize - paddingLeft - paddingRight;
        int tempNum = 1;
        int tempLine = 1;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            measureChildWithMargins(childView, widthSize, 0, heightSize, 0);

            LayoutParams lp = (LayoutParams) childView.getLayoutParams();

            //子view所占的宽度
            int childViewMeasuredWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //子view所占的高度
            int childViewMeasuredHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //换行
            if (horizontalWidth + childViewMeasuredWidth > lineWidth || lp.layout_new_line) {
                /*每行view的数量*/
                lineView.add(eachLineView);

                eachLineView = new ArrayList<>();
                eachLineView.add(childView);

                int free = lineWidth - (horizontalWidth - getHGap());
                //记录每行的left距离
                int left = (int) (free * getHorizontalOffsetScale());

                eachLineFreeWidth.add(eachViewTotalWidth);

                eachLineLeft.add(left);

                //获取最大宽度的一行
                resultWidth = Math.max(resultWidth, horizontalWidth);
                //如果换行，从第下一行开始计算每行宽度
                horizontalWidth = childViewMeasuredWidth + getHGap();
                eachViewTotalWidth = childViewMeasuredWidth;


                resultHeight += verticalHeight;
                //记录换行时上行最大的view高度
                eachLineHeight.add(verticalHeight);

                //换行后保存下一行的view高度
                verticalHeight = childViewMeasuredHeight;


                //判断是否超过行数限制
                if (getMaxLine() > 0 && tempLine >= getMaxLine()) {
                    break;
                }
                tempLine += 1;
            } else {
                eachLineView.add(childView);
                //如果不换行，累加每行的view宽度以及view间隙
                horizontalWidth = horizontalWidth + childViewMeasuredWidth + getHGap();
                eachViewTotalWidth = eachViewTotalWidth + childViewMeasuredWidth;
                //如果不换行，计算每行最高view的高度
                verticalHeight = Math.max(verticalHeight, childViewMeasuredHeight);
            }
            if (i == (childCount - 1)) {
                lineView.add(eachLineView);

                eachLineFreeWidth.add(eachViewTotalWidth);

                //记录每行的left距离
                int left = (int) ((lineWidth - (horizontalWidth - getHGap())) * getHorizontalOffsetScale());
                eachLineLeft.add(left);
                //记录最后一行最大的view高度
                eachLineHeight.add(verticalHeight);

                //最后需要减去多余的一个间隙
                resultWidth = Math.max(resultWidth, horizontalWidth) - getHGap();
                resultHeight = resultHeight + verticalHeight;

                //判断是否超过行数限制
                if (getMaxLine() > 0 && tempLine >= getMaxLine()) {
                    break;
                }
                tempLine += 1;
            }


            if (getMaxNum() > 0 && tempNum >= getMaxNum()) {
                /*因为有限制，所以需要提前结束并添加view*/
                if (i != (childCount - 1)) {
                    lineView.add(eachLineView);
                    eachLineFreeWidth.add(eachViewTotalWidth);
                    //记录每行的left距离
                    int left = (int) ((lineWidth - (horizontalWidth - getHGap())) * getHorizontalOffsetScale());
                    eachLineLeft.add(left);
                    //记录最后一行最大的view高度
                    eachLineHeight.add(verticalHeight);

                    //最后需要减去多余的一个间隙
                    resultWidth = Math.max(resultWidth, horizontalWidth) - getHGap();
                    resultHeight = resultHeight + verticalHeight;
                }
                break;
            }
            tempNum += 1;
        }

        resultWidth = resultWidth + paddingLeft + paddingRight;
        resultHeight = resultHeight + getPaddingTop() + getPaddingBottom()+(lineView.size()-1)*getVGap();

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : resultWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : resultHeight);
    }

    private boolean isAlignGravity(){
        return getGravity() == gravity_left_right_align||getGravity() == gravity_left_right_align_ignore_last_line;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int measuredWidth = getMeasuredWidth();
        int mPaddingLeft = getPaddingLeft();
        int mPaddingTop = getPaddingTop();
        int leftLocation = 0;
        int topLocation = 0;
        int lineCount = lineView.size();
        boolean isLeftRightAlign =isAlignGravity();
        for (int i = 0; i < lineCount; i++) {
            int lineMaxHeight = eachLineHeight.get(i);
            List<View> lineViews = lineView.get(i);
            int size = lineViews.size();
            int eachItemGap = 0;
            if (isLeftRightAlign) {
                if (size <= 1) {
                    eachItemGap = 0;
                } else {
                    eachItemGap = (measuredWidth-mPaddingLeft-getPaddingRight() - eachLineFreeWidth.get(i)) / (size - 1);
                }

                if(i==(lineCount-1)&&getGravity()==gravity_left_right_align_ignore_last_line){
                    eachItemGap=getHGap();
                }
                if (eachItemGap < 0) {
                    eachItemGap = 0;
                }
                leftLocation = 0;
            } else {
                leftLocation = eachLineLeft.get(i);
            }
            for (int j = 0; j < size; j++) {
                View childView = lineViews.get(j);
                if (childView.getVisibility() == View.GONE) {
                    continue;
                }
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();

                int topOffset = 0;
                //只有某行的view高度低于那行最大高度时才设置偏移
                if (childHeight < lineMaxHeight) {
                    topOffset = (int) ((lineMaxHeight - childHeight) * getVerticalOffsetScale());
                }
                LayoutParams lp = (LayoutParams) childView.getLayoutParams();
                childView.layout(
                        mPaddingLeft + leftLocation + lp.leftMargin,
                        mPaddingTop + topLocation + lp.topMargin + topOffset,
                        mPaddingLeft + leftLocation + lp.leftMargin + childWidth,
                        mPaddingTop + topLocation + lp.topMargin + childHeight + topOffset);
                if (isLeftRightAlign) {
                    leftLocation += childWidth + lp.leftMargin + lp.rightMargin + eachItemGap;
                } else {
                    leftLocation += childWidth + lp.leftMargin + lp.rightMargin + getHGap();
                }
            }
            topLocation += (lineMaxHeight + getVGap());
        }

    }

    private float getHorizontalOffsetScale() {
        return getGravity() / 2f;
    }

    private float getVerticalOffsetScale() {
        return getGravityVertical() / 2f;
    }

    public void setBothGap(int bothGap) {
        setHGap(bothGap);
        setVGap(bothGap);
    }

    public int getMaxNum() {
        if (maxNum < 0) {
            maxNum = 0;
        }
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        if (maxNum < 0) {
            maxNum = 0;
        }
        if (this.maxNum != maxNum && maxNum >= 0) {
            this.maxNum = maxNum;
            requestLayout();
        }
    }

    public int getMaxLine() {
        if (maxLine < 0) {
            maxLine = 0;
        }
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        if (maxLine < 0) {
            maxLine = 0;
        }
        if (this.maxLine != maxLine && maxLine >= 0) {
            this.maxLine = maxLine;
            requestLayout();
        }
    }

    public int getVGap() {
        return vGap;
    }

    public FlowLayout setVGap(int vGap) {
        if (vGap < 0) {
            vGap = 0;
        }
        if (this.vGap != vGap) {
            this.vGap = vGap;
            requestLayout();
        }
        return this;
    }

    public int getHGap() {
        return hGap;
    }

    public FlowLayout setHGap(int hGap) {
        if (hGap < 0) {
            hGap = 0;
        }
        if (this.hGap != hGap) {
            this.hGap = hGap;
            requestLayout();
        }
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public FlowLayout setGravity(@gravity_type int gravity) {
        if (gravity != gravity_left
                && gravity != gravity_center
                && gravity != gravity_right
                && gravity != gravity_left_right_align
                && gravity != gravity_left_right_align_ignore_last_line
        ) {
            gravity = gravity_left;
        }
        if (this.gravity != gravity) {
            this.gravity = gravity;
            requestLayout();
        }
        return this;
    }

    public int getGravityVertical() {
        return gravityVertical;
    }

    public FlowLayout setGravityVertical(@gravity_type_vertical int gravityVertical) {
        if (gravityVertical != gravity_top && gravityVertical != gravity_center && gravityVertical != gravity_bottom) {
            gravityVertical = gravity_top;
        }
        if (this.gravityVertical != gravityVertical) {
            this.gravityVertical = gravityVertical;
            requestLayout();
        }
        return this;
    }

    @Override
    public FlowLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FlowLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof FlowLayout.LayoutParams) {
            return new FlowLayout.LayoutParams((FlowLayout.LayoutParams) lp);
        } else if (lp instanceof MarginLayoutParams) {
            return new FlowLayout.LayoutParams((MarginLayoutParams) lp);
        }
        return new FlowLayout.LayoutParams(lp);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean layout_new_line = false;
//        public int layout_left_gap =0;
//        public int layout_right_gap =0;
//        public int layout_top_gap =0;
//        public int layout_bottom_gap =0;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
            layout_new_line = array.getBoolean(R.styleable.FlowLayout_Layout_layout_new_line, false);
//            layout_left_gap = (int) array.getDimension(R.styleable.FlowLayout_Layout_layout_left_gap, 0);
//            layout_right_gap =(int) array.getDimension(R.styleable.FlowLayout_Layout_layout_right_gap, 0);
//            layout_top_gap =(int) array.getDimension(R.styleable.FlowLayout_Layout_layout_top_gap, 0);
//            layout_bottom_gap =(int) array.getDimension(R.styleable.FlowLayout_Layout_layout_bottom_gap, 0);

            array.recycle();
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

        public LayoutParams(@NonNull FlowLayout.LayoutParams source) {
            super(source);
        }
    }
}
