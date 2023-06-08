package com.example.lottery.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.lottery.MainActivity;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static InputImage getImage(Context context, String fileName) {
        Uri uri = Uri.fromFile(new File(context.getFilesDir(), fileName));
        return getImage(context, uri);
    }

    public static InputImage getImage(Context context, Uri uri) {
        InputImage inputImage = null;
        try {
            inputImage = InputImage.fromFilePath(context, uri);
        } catch (IOException e) {

        }
        return inputImage;
    }
}
