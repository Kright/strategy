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

    public Village(Country country, Cell cell) {
        super(country, cell);
    }

    @Override
    public int getTaxes() {
        return 2;
    }

    @Override
    public void nextTurn() {

    }

    @Override
    public void render(Canvas canv, Rect r, Paint paint) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, paint);
    }
}
