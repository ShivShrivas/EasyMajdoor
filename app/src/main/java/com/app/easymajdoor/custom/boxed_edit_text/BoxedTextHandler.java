package com.app.easymajdoor.custom.boxed_edit_text;

import android.text.Editable;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.List;

import com.app.easymajdoor.utils.SimpleTextWatcher;

import timber.log.Timber;

public class BoxedTextHandler {
    private final List<EditText> dashedText;
    private BoxedTextFilledListener filledListener;
    private BoxETListener boxETListener = new BoxETListener();

    public BoxedTextHandler(List<EditText> dashedText) {
        this.dashedText = dashedText;
    }

    public void setFillerListener(BoxedTextFilledListener filledListener){
        this.filledListener = filledListener;
    }

    public void setCallback(BoxETListener boxETListener) {
        this.boxETListener = boxETListener;
    }

    private static int getArrayNext(int current, int size) {
        int next = current + 1;
        if (next == size) {
            next -= 1;
        }
        return next;
    }

    private static int getArrayPrev(int current) {
        int next = current - 1;
        if (next == -1) {
            next = 0;
        }
        return next;
    }

    public void initialize(){
        for (int i = 0; i < dashedText.size(); i++) {
            final EditText current = dashedText.get(i);
            final EditText prev = dashedText.get(getArrayPrev(i));
            final EditText next = dashedText.get(getArrayNext(i, dashedText.size()));
            final EditText farNext = dashedText.get(getArrayNext(getArrayNext(i, dashedText.size()), dashedText.size()));

            current.setFreezesText(true);
            if(filledListener != null) {
                current.addTextChangedListener(filledListener);
            }
            current.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    Timber.d("after: %s", s);

                    if (s.length() > 1) {
                        if (current.getSelectionEnd() == current.length()) {
                            next.setText(String.valueOf(s.charAt(1)));
                            current.setText(String.valueOf(s.charAt(0)));
                            farNext.requestFocus();
                        } else {
                            current.setText(String.valueOf(s.charAt(0)));
                            next.setSelection(0);
                        }

                    } else if (s.length() == 1) {
                        next.requestFocus();
                    }

                    if ((current == next) && s.length() >= 1
                            && (filledListener == null || filledListener.isFilled())) {
                        boxETListener.onReachedEnd();
                    }
                }
            });

            current.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_DEL) {

                    if (current.getSelectionEnd() == current.length()) {
                        current.setText("");
                    }
                    prev.requestFocus();
                    prev.setSelection(prev.getText().length());
                }

                return false;
            });
        }
    }

}