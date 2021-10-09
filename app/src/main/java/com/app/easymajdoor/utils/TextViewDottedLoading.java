package com.app.easymajdoor.utils;

import android.os.Handler;
import android.widget.TextView;

public class TextViewDottedLoading implements Runnable {
    private static final int ANIM_STOPPED = -1000;
    final Handler handler = new Handler();
    private TextView textView;
    private int duration;
    private String text;
    private int count = ANIM_STOPPED;
    private boolean isRunning = false;

    public TextViewDottedLoading(TextView textView, int duration) {
        this.textView = textView;
        this.text = text;
        this.duration = duration;
    }

    public void setText(String text, boolean shouldAnimate) {
        this.text = text;
        textView.setText(text);
        if (shouldAnimate) startAnim(); else stopAnim();
    }

    public void startAnim(){
        count = 0;
        if (!isRunning) {
            handler.post(this);
        }
    }

    public void stopAnim(){
        count = ANIM_STOPPED;
    }

    @Override
    public void run() {
        isRunning = true;
        if (count == 0){
            textView.setText(text);
        } else if (count == 1) {
            textView.setText(text + " .");
        } else if (count == 2) {
            textView.setText(text + " . .");
        } else if (count == 3) {
            textView.setText(text + " . . .");
            count = -1;
        }
        count++;

        if(count >= 0) {
            handler.postDelayed(this, duration);
        } else {
            isRunning = false;
        }
    }

}