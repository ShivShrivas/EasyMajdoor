package com.app.easymajdoor.utils;

import android.view.View;
import android.widget.Toast;

import com.app.easymajdoor.R;
import com.google.android.material.snackbar.Snackbar;

public class Message {
    public final View anchorView;

    public Message(View anchor) {
        this.anchorView = anchor;
    }

    public void showSnackBar(String text) {
        getSnackBar(text).show();
    }

    public void showSnackBar(int textId) {
        getSnackBar(textId).show();
    }

    public Snackbar getSnackBar(int textId) {
        return Snackbar.make(anchorView, textId, Snackbar.LENGTH_SHORT);
    }

    public Snackbar getSnackBar(String text) {
        return Snackbar.make(anchorView, text, Snackbar.LENGTH_SHORT);
    }

    public void showToast(String text) {
        Toast.makeText(anchorView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
