package com.example.dk88.Model;

import android.graphics.Bitmap;

public class Picture {
    private Bitmap bitmap;

    public Picture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Picture() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
