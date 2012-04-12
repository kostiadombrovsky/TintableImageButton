package com.kdombrovsky.tintableimagebutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class TintableButton extends Button
{
    private int            tintUpdateDelay_;
    private ColorStateList colorStateList_;

    //==================================================================================================================
    public TintableButton(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintableImageButton);
        colorStateList_ = typedArray.getColorStateList(R.styleable.TintableImageButton_tintColorStateList);
        tintUpdateDelay_ = typedArray.getInt(R.styleable.TintableImageButton_tintUpdateDelay, 300);
        typedArray.recycle();

        String focusable = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "focusable");
        if (focusable != null)
            setFocusable(Boolean.parseBoolean(focusable));
    }

    //==================================================================================================================
    @Override
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();

        if (colorStateList_ == null)
            return;

        if (tintUpdateDelay_ <= 0)
            updateTint();
        else
        {
            //Delay tint update so the user can see it after he lifts his finger
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() { public void run() { updateTint(); } }, tintUpdateDelay_);
        }
    }

    //==================================================================================================================
    private void updateTint()
    {
        if (colorStateList_ == null)
            return;

        int tint = colorStateList_.getColorForState(getDrawableState(), 0x00000000);
        getBackground().setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
        invalidate();
    }

    //==================================================================================================================
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);
        updateTint();
        return result;
    }
}