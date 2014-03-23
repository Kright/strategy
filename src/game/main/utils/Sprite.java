package game.main.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

/**
 * спрайт. Содержит картинку и прямоугольник - то место картинки, которое надо использовать
 * Created by lgor on 17.01.14.
 */
public class Sprite {

    public final Bitmap bmp;
    public final Rect rect;

    public Sprite(Bitmap bmp, int x, int y, int width, int height) {
        this.bmp = bmp;
        rect = new Rect(x, y, x + width, y + height);
    }

    public static Bitmap loadBmp(Resources res, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(res, id, options);
        Log.d("action", "Bmp loaded as " + bmp.getWidth() + "x" + bmp.getHeight());
        return bmp;
    }

    public static Sprite[] fromBmp(Bitmap bmp, int count, int w, int h, int dh) {
        Sprite[] sp = new Sprite[count];
        for (int i = 0; i < count; i++) {
            sp[i] = new Sprite(bmp, w * i, dh, w, h - dh);
        }
        return sp;
    }
}
