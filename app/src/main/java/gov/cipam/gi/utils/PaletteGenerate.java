package gov.cipam.gi.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Window;

/**
 * Created by karan on 1/2/2018.
 */

public class PaletteGenerate {

    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
            }
        });
    }

    private Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }

    private Palette.Swatch checkVibrantSwatch(Palette p) {
        Palette.Swatch vibrant = p.getVibrantSwatch();
        if (vibrant != null) {
            return vibrant;
        }
        return p.getDarkVibrantSwatch();
    }

    public void setToolbarColor(Bitmap bitmap,Toolbar toolbar) {

        if(bitmap!=null) {
            Palette p = createPaletteSync(bitmap);
            Palette.Swatch vibrantSwatch = checkVibrantSwatch(p);

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.setStatusBarColor(vibrantSwatch.getRgb());*/

            toolbar.setBackgroundColor(vibrantSwatch.getRgb());
        }
    }
}
