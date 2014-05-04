package com.vk.lgorsl.utils.sprites;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * спрайт. Содержит картинку и прямоугольник - то место картинки, которое надо использовать
 * класс SpriteBank аккуратно загружает картинки, а потом может "обновить" bitmap, если приложение сворачивали или
 * вообще загрузили из сохранения
 * Теоретически, его можно использовать не по назначению - рисовать не через render(), а вручную
 * Created by lgor on 17.01.14.
 */
public class Sprite implements iRender {

    transient public Bitmap bmp;
    transient protected Rect rect;

    public Sprite(Bitmap bmp, int x, int y, int width, int height) {
        this.bmp = bmp;
        rect = new Rect(x, y, x + width, y + height);
    }

    @Override
    public void render(RenderParams p) {
        try {
            p.rect.set(p.x, p.y, p.x + p.width, p.y + p.height);
            p.canvas.drawBitmap(bmp, rect, p.rect, p.paint);
        } catch (NullPointerException ex) {
            throw new NullPointerException("Sprite doesn't load correctly !\n");
        }
    }
}
