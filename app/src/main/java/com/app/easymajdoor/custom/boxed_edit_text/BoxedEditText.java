package com.app.easymajdoor.custom.boxed_edit_text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.app.easymajdoor.R;

import java.util.ArrayList;
import java.util.List;

public class BoxedEditText extends LinearLayout {
    private final int DEF_TEXT_SIZE_SP = 18;

    private int numBoxes = 4;
    @ColorInt private int tint;
    private float textSize;
    private final List<EditText> ets = new ArrayList<>();
    private BoxedTextFilledListener boxedTextFilledListener;
    private BoxedTextHandler boxedTextHandler;
    private int ETSize;

    public BoxedEditText(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BoxedEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BoxedEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public BoxedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BoxedEditText, 0, 0);

        int colorAccent = getColor(context, R.attr.colorAccent);

        try {
            tint = a.getColor(R.styleable.BoxedEditText_tint, colorAccent);
            numBoxes = a.getInt(R.styleable.BoxedEditText_numBoxes, 4);
            textSize = a.getDimension(R.styleable.BoxedEditText_textSize, spToPx(DEF_TEXT_SIZE_SP, context));

        } finally {
            a.recycle();
        }

        for (int i = 0; i < numBoxes; i++) {
            EditText et = getStyledET(context);
            ETSize = (int) (4*textSize);

            GradientDrawable drawable = (GradientDrawable) et.getBackground();
            drawable.setStroke(ETSize/20, tint);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ETSize, ETSize);
            if (i != numBoxes - 1) {
                params.setMarginEnd(ETSize / 4);
            }
            et.setLayoutParams(params);

            addView(et);
            ets.add(et);
        }

        boxedTextFilledListener = new BoxedTextFilledListener(0, numBoxes);
        boxedTextHandler = new BoxedTextHandler(ets);
        boxedTextHandler.setFillerListener(boxedTextFilledListener);
        boxedTextHandler.initialize();
        setEnabled(true);
    }

    private EditText getStyledET(Context context) {
        EditText et = new EditText(context);
        et.setId(View.generateViewId());
        et.setTextSize(textSize);
        et.setPadding(0, 0, 0, 0);
        et.setIncludeFontPadding(false);
        et.setBackground(ContextCompat.getDrawable(context, R.drawable.otp_box_bg));
        et.setGravity(Gravity.CENTER);
        et.setHint(R.string._0);
        et.setCursorVisible(false);
        et.setHintTextColor(ContextCompat.getColor(context, R.color.light_grey));
        et.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(2) });
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        return et;
    }

    private int getColor(Context context, int colorAttr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { colorAttr });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public void setTint(int tint) {
        this.tint = tint;

        for (int i = 0; i < numBoxes; i++) {
            GradientDrawable drawable = (GradientDrawable) ets.get(i).getBackground();
            drawable.setStroke(ETSize/20, tint);
        }
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public void setText(String text) {
        for (int i = 0; i < Math.min(text.length(), numBoxes); i++) {
            ets.get(i).setText(text.charAt(i));
        }
    }

    public void setBoxETListener(BoxETListener boxETListener) {
        boxedTextHandler.setCallback(boxETListener);
        boxedTextFilledListener.setCallback(boxETListener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < numBoxes; i++) {
            ets.get(i).setEnabled(enabled);
        }
    }

}
