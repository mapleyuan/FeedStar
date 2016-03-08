package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 带点击的效果
 * Created by yuanweinan on 16-1-20.
 */
public class SimpleImageView extends ImageView {

    public SimpleImageView(Context context) {
        super(context);
    }

    public SimpleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(background);
        super.setBackgroundDrawable(layer);
    }

    /**
     *
     */
    protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

        // The color filter to apply when the button is pressed
        protected ColorFilter mPressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        // Alpha value when the button is disabled
        protected int mDisabledAlpha = 50;

        public SAutoBgButtonBackgroundDrawable(Drawable d) {
            super(new Drawable[] { d });
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean enabled = false;
            boolean pressed = false;

            for (int state : states) {
                if (state == android.R.attr.state_enabled) {
                    enabled = true;
                }
                else if (state == android.R.attr.state_pressed) {
                    pressed = true;
                }
            }

            mutate();
            if (enabled && pressed) {
                setColorFilter(mPressedFilter);
            } else if (!enabled) {
                setColorFilter(null);
                setAlpha(mDisabledAlpha);
            } else {
                setColorFilter(null);
            }

            invalidateSelf();

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }
}
