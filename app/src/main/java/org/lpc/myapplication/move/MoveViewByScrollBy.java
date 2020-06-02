package org.lpc.myapplication.move;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 通过父view的scrollBy方法水平移动view
 */
public class MoveViewByScrollBy extends View {
    private int[] parentPos = new int[2];
    private int[] selfPos = new int[2];

    private int[] selfPosStart = new int[2];
    private int mParentWidth;
    private int mWidth;

    private int lastX;
    private int lastY;
    public MoveViewByScrollBy(Context context) {
        super(context);
        init(context);
    }

    public MoveViewByScrollBy(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveViewByScrollBy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveViewByScrollBy(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        setBackgroundColor(Color.rgb(255,255,0));
        post(new Runnable() {
            @Override
            public void run() {
                if (mWidth == 0) {
                    mWidth = getWidth();
                }
                if (mParentWidth == 0) {
                    mParentWidth = ((View) getParent()).getWidth();
                }
                ((View) getParent()).getLocationOnScreen(selfPosStart);
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

                break;
            case MotionEvent.ACTION_MOVE:

                //计算移动的距离
                int offX = x - lastX;
                CallBean callBean = noReachParentBorder(offX);
                if (callBean.noReachBorder) {
                    int left = this.getLeft() + callBean.lastOffX;
                    coreMoveLogic(left);
                }

                break;
        }
        return true;
    }

    private void coreMoveLogic(int left) {
//        left = left >= 0 ? left : 0;

        int right = left + mWidth;
        if (right < mParentWidth) {

        } else {
            right = mParentWidth;
            left = mParentWidth - mWidth;
        }
        Log.e("offX", right + "--" + left);
        ((View) getParent()).scrollBy(getLeft()-left,0);
    }

    public CallBean noReachParentBorder(int offX) {
//        ((View) getParent()).getLocationOnScreen(parentPos);
        this.getLocationOnScreen(selfPos);
        CallBean callBean = new CallBean();
        callBean.lastOffX = offX;
        Log.e("pos", selfPos[0] + "--" + selfPosStart[0] + "--" + offX + "--" + selfPos[1]+ "--" + selfPosStart[1]+ "--" +mParentWidth+ "--" +mWidth );
        if (selfPos[0]>= selfPosStart[0] &&
                selfPos[0] <= (mParentWidth-mWidth)) {
            callBean.noReachBorder = true;
            if (selfPos[0]+offX<=selfPosStart[0]){
                callBean.lastOffX = selfPosStart[0]-selfPos[0];
            }if (selfPos[0]+offX>=(mParentWidth-mWidth)){
                callBean.lastOffX =(mParentWidth-mWidth)-selfPos[0];
            }
        }else {
            callBean.noReachBorder = false;
        }


        Log.e("callBean",callBean.toString());
        return callBean;
    }

    class CallBean {
        boolean noReachBorder;
        int lastOffX;

        @Override
        public String toString() {
            return "CallBean{" +
                    "noReachBorder=" + noReachBorder +
                    ", lastOffX=" + lastOffX +
                    '}';
        }
    }
}
