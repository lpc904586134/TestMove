package org.lpc.myapplication.move;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 通过Scroller移动view
 */
public class MoveViewByScroller extends View {

    private int mParentWidth;
    private int mWidth;

    private int lastX;
    private int lastY;
    private Scroller mScroller;
    public MoveViewByScroller(Context context) {
        super(context);
        init(context);
    }

    public MoveViewByScroller(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveViewByScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveViewByScroller(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        mScroller = new Scroller(context);
        setBackgroundColor(Color.rgb(0,255,0));
        post(new Runnable() {
            @Override
            public void run() {
                if (mWidth == 0) {
                    mWidth = getWidth();
                }
                if (mParentWidth == 0) {
                    mParentWidth = ((View) getParent()).getWidth();
                }
                Log.e("post",mWidth+"--"+mParentWidth);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ACTION_DOWN", x + "");
                lastX = x;
                lastY = y;

                break;
            case MotionEvent.ACTION_UP:
                View viewGroup= (View) getParent();
                mScroller.startScroll(viewGroup.getScrollX(),
                        viewGroup.getScrollY(),
                        -viewGroup.getScrollX(),
                        -viewGroup.getScrollY());//返回原来的位置
                //通过重绘不断调用computeScroll
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                //计算移动的距离
                int offsetX=x-lastX;
                int offsetY=y-lastY;
                ((View)getParent()).scrollBy(-offsetX,-offsetY);

                break;
        }
        return true;
    }



    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //通过重绘不断调用computeScroll
            invalidate();
        }
    }
}
