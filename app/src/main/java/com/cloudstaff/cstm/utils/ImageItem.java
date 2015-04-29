package com.cloudstaff.cstm.utils;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap bitmapImage;

    public ImageItem(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }
}