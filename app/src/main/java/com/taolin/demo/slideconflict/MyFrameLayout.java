package com.taolin.demo.slideconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author taolin
 * @version v1.0
 * @date 2019/04/01
 * @description
 */
public class MyFrameLayout extends FrameLayout {

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Down事件要流到子view，由子view判断是否需要父view拦截
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
            return true;
        }
    }

    private float rowY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Taolin", "parent onTouchEvent "  + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                LayoutParams l = (LayoutParams) getLayoutParams();
                // 计算当前位置离父view顶部的距离
                rowY = event.getRawY() - l.topMargin;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                LayoutParams l = (LayoutParams) getLayoutParams();
                if (rowY == -1) {
                    // 从子view给过来的后续事件，不包含Down，所以这里要初始化一下
                    rowY = event.getRawY() - l.topMargin;
                }
                // 当前位置减去距离顶部的距离，就是marginTop
                l.topMargin = (int)(event.getRawY() - rowY);
                setLayoutParams(l);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 事件结束的时候要把坐标还原，否则影响下次事件
                rowY = - 1;
                break;

            default:break;

        }
        return true;
    }
}
