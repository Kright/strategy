package game.main.utils.sprites;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Хитрый спрайт
 * Может рисовать себя так, как будто он больше или меньше, чем область, в которой его рисуют.
 * Коварства ему не занимать
 * Created by lgor on 07.04.14.
 */
public class AdvancedSprite extends Sprite implements Serializable{

    private final float kx, ky, kw, kh;

    /**
     *
     * @param bmp - битмап
     * @param x     координата левого верхнего угла на bitmap
     * @param y     тоже
     * @param width     ширина кусочка Bitmap, который берётся за образец
     * @param height    высота
     * @param dx        смещение относительно истинного размера влево
     * @param dy        аналогично вниз
     * @param cellW     ширина спрайта, которого из себя изображает наш спрайт
     * @param cellH     высота
     */
    public AdvancedSprite(Bitmap bmp, int x, int y, int width, int height, int dx, int dy, int cellW, int cellH) {
        super(bmp, x, y, width, height);
        this.kx = ((float)dx)/cellW;
        this.ky = ((float)dy)/cellH;
        this.kw = ((float)width)/cellW;
        this.kh = ((float)height)/cellH;
    }

    /**
     * рисует спрайт с заданными параметрами (большинство из них одинаковы во времени, поэтому передаются ссылкой)
     *
     * @param p - параметры рисования
     */
    public void render(RenderParams p) {
        int x = p.x+(int)(kx*p.width);
        int y = p.y+(int)(ky*p.height);
        p.rect.set(x, y, x + (int)(p.width*kw+1), y + (int)(p.height*kh));
        p.canvas.drawBitmap(bmp, rect, p.rect, p.paint);
    }
}
