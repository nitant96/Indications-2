package gov.cipam.gi.utils;

/**
 * Created by karan on 12/29/2017.
 */
import android.annotation.TargetApi;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class CropSquareTransformation implements Transformation {
    @TargetApi(19)
    @Override
    public Bitmap transform(final Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }


    @Override
    public String key() {
        return "square()";
    }
}