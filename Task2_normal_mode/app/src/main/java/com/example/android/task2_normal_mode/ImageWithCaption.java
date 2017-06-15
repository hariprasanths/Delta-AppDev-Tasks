package com.example.android.task2_normal_mode;

import android.graphics.Bitmap;

/**
 * Created by Hari on 13-06-2017.
 */

public class ImageWithCaption {
    private Bitmap mimageResouseID;
    private String mcaption;
    public ImageWithCaption(Bitmap imageResourseID,String caption)
    {
        mimageResouseID = imageResourseID;
        mcaption = caption;

    }
    public Bitmap getimageResouseID()
    {
        return mimageResouseID;
    }
    public  String getcaption()
    {
        return mcaption;
    }

}
