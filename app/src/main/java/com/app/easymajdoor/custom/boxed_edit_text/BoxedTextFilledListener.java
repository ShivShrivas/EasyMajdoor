package com.app.easymajdoor.custom.boxed_edit_text;

import android.text.Editable;
import android.text.TextWatcher;

public class BoxedTextFilledListener implements TextWatcher {

    private int numFilled;
    private final int size;
    private BoxETListener boxETListener;

    public BoxedTextFilledListener(int numFilled, int size){
        this.numFilled = numFilled;
        this.size = size;
        this.boxETListener = new BoxETListener();
    }

    public BoxedTextFilledListener(int numFilled, int size, BoxETListener boxETListener){
        this(numFilled, size);
        this.boxETListener = boxETListener;
    }

    public boolean isFilled(){
        return numFilled == size;
    }

    public void setCallback(BoxETListener boxETListener) {
        this.boxETListener = boxETListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() == 0 && before == 1){
            numFilled -= 1;
        }else if(s.length() == 1 && before == 0) {
            numFilled += 1;
        }
        if(numFilled < 0 || numFilled > size){
            numFilled = 0;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        boxETListener.onChange(isFilled());
    }
}