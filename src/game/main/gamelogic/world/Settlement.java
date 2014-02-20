package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.gamelogic.GameSession;
import game.main.utils.Sprite;

/**
 * Created by lgor on 25.01.14.
 * поселение. Думаю потом унаследовать от него деревушку и город.
 */
public class Settlement implements iRender {

    private static Sprite[] samples;
    public final int playerID;
    protected Cell cell;
    private Sprite sprite;

    public Settlement(Cell cell, int playerID) {
        this.playerID = playerID;
        this.cell = cell;
        sprite = samples[GameSession.now.rnd.nextInt(samples.length)];
    }

    public static void init(Sprite[] sprites) {
        samples = sprites;
    }

    public void nextTurn() {
        //заглушка
    }

    public void render(Canvas canv, Rect r) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, null);
    }

    public void render(Canvas canv, Rect r, Paint paint) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, paint);
    }
}
