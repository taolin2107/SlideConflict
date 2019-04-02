package com.taolin.demo.slideconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author taolin
 * @version v1.0
 * @date 2019/04/01
 * @description
 */
public class MyView extends View {

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float rowY;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Taolin", "child onTouchEvent "  + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true);
                FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) getLayoutParams();
                rowY = event.getRawY() - l.topMargin;
                lastY = event.getRawY();
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) getLayoutParams();
                float currentY = event.getRawY();
                float marginTop = l.topMargin;
                if (marginTop < 2) {
                    if (lastY > currentY) {
                        // 滑到顶部了，继续往上滑就交给父view
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                } else if (marginTop > 328) {
                    if (lastY < currentY) {
                        // 滑到底部了，继续往下滑就交给父view
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                }
                l.topMargin = (int)(currentY - rowY);
                lastY = currentY;
                setLayoutParams(l);
                return true;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;

            default:break;
        }
        return false;
    }
}
