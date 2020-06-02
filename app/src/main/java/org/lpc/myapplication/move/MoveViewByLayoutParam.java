package org.lpc.myapplication.move;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 通过设置MarginLayoutParams方法水平移动view
 */
public class MoveViewByLayoutParam extends View {
    private int[] parentPos = new int[2];
    private int[] selfPos = new int[2];
    private int mParentWidth;
    private int mWidth;

    private int lastX;
    private int lastY;
    public MoveViewByLayoutParam(Context context) {
        super(context);
        init(context);
    }

    public MoveViewByLayoutParam(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveViewByLayoutParam(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoveViewByLayoutParam(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        setBackgroundColor(Color.rgb(255, 125, 0));
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

                break;
            case MotionEvent.ACTION_MOVE:

                //计算移动的距离
                int offX = x - lastX;
                if (noReachParentBorder()) {
                    int left = this.getLeft() + offX;
                    coreMoveLogic(left);
                }

                break;
        }
        return true;
    }

    private void coreMoveLogic(int left) {
        left = left >= 0 ? left : 0;

        int right = left + mWidth;
        if (right < mParentWidth) {

        } else {
            right = mParentWidth;
            left = mParentWidth - mWidth;
        }
        Log.e("offX", right + "--" + left);
        ViewGroup.MarginLayoutParams mlp =
                (ViewGroup.MarginLayoutParams) getLayoutParams();
        mlp.leftMargin = left;
        setLayoutParams(mlp);
    }

    public boolean noReachParentBorder() {
        ((View) getParent()).getLocationOnScreen(parentPos);
        this.getLocationOnScreen(selfPos);
        Log.e("pos", parentPos[0] + "--" + selfPos[0] + "--" + mParentWidth + "--" + mWidth);
        if (parentPos[0] <= selfPos[0] &&
                (mParentWidth + parentPos[0]) >= (selfPos[0] + mWidth)) {
            return true;
        }
        return false;
    }

}
