package com.hzdq.bajiesleepchildrenHD.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by 郝悦 on 2018/1/11.
 */

public class MyCustomCheckbox extends androidx.appcompat.widget.AppCompatCheckBox {
    private static final String TAG = MyCustomCheckbox.class.getSimpleName();

    public MyCustomCheckbox(Context context) {
        super(context);
    }

    public MyCustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[0];
        int gravity = getGravity();
        int left = 0;
        if (gravity == Gravity.CENTER) {
            left = ((int) (getWidth() - drawable.getIntrinsicWidth() - getPaint().measureText(getText().toString()))
                    / 2);
        }
        drawable.setBounds(left, 0, left + drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }
}