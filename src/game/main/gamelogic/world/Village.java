package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.utils.Sprite;

/**
 * Деревушка
 * Created by lgor on 14.03.14.
 */
public class Village extends Settlement {

    protected static Sprite sprite;

    public Village(Cell cell, int countryID) {
        super(cell, countryID);
    }

    @Override
    public void nextTurn() {

    }

    @Override
    public void render(Canvas canv, Rect r, Paint paint) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, paint);
    }
}
