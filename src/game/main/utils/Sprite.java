package game.main.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
/**
 * спрайт. Содержит картинку и прямоугольник - то место картинки, которое надо использовать
 * класс SpriteBank аккуратно загружает картинки, а потом может "обновить" bitmap, если приложение сворачивали или
 * вообще загрузили из сохранения
 * Created by lgor on 17.01.14.
 */
public class Sprite {

    transient public Bitmap bmp;
    public final Rect rect;

    public Sprite(Bitmap bmp, int x, int y, int width, int height) {
        this.bmp = bmp;
        rect = new Rect(x, y, x + width, y + height);
    }
}
