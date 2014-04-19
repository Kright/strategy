package game.main.gamelogic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import game.main.gamelogic.world.Map;
import game.main.utils.sprites.SpriteBank;

/**
 * вообще, очень костыльный метод - можно рисовать картинку в текстуру меньшего размера, а потом её растягивать на весь
 * экран.
 * Результаты тестирования на Sony SL:
 * без масштабирования (MapRender)  -   12-14 fps
 * scale =  2/3                     -   14-16 fps
 * scale = 1/2                      -   18-21 fps
 *
 * Created by lgor on 19.04.14.
 */
public class BufferedRender extends MapRender {

    private Bitmap buf;
    private Canvas canvas;
    private Matrix smaller, bigger;
    private Paint p = new Paint();

    public BufferedRender(int spriteHeight, SpriteBank sprites, GameProperties properties) {
        super(spriteHeight, sprites, properties);
        buf = Bitmap.createBitmap(128,128, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(buf);
        smaller = new Matrix();
        bigger = new Matrix();
        //bigger.setScale(1.5f,1.5f);
        //smaller.setScale(2f/3, 2f/3);
        bigger.setScale(2,2);
        smaller.setScale(0.5f, 0.5f);
    }

    private int w,h;

    @Override
    public void render(Canvas canv, Map map) {
        checkBuf(canv.getWidth()/2, canv.getHeight()/2);
        //checkBuf(canv.getWidth()/3*2, canv.getHeight()/3*2);
        w = canv.getWidth();
        h = canv.getHeight();
        super.render(canvas, map);
        canv.drawBitmap(buf, bigger, p);
    }

    private void checkBuf(int w, int h){
        if (buf.getWidth()!= w || buf.getHeight() != h){
            buf = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(buf);
            canvas.setMatrix(smaller);
        }
    }

    public CellIterator initRender(Canvas canv, Map map) {
        setScreenSize(w, h);
        checkPosition(screenW, screenH, map.width * w, map.height * dy + h - dy);
        renderParams.setCellSize((int) getCellWidth() + 1, (int) getCellHeight() + 1);
        renderParams.canvas = canv;
        canv.drawColor(0xFF444444); //фон
        return getIterator(map, renderParams);
    }
}
