package game.main.gamelogic.meta;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.Sprite;
import game.main.GUI.iRender;
import game.main.gamelogic.GameSession;

/**
 * Created by lgor on 25.01.14.
 */
public class Settlement implements iRender{

    private static Sprite[] samples;

    /**
     * поселение. Думаю потом унаследовать от него деревушку и город.
     */
    private Sprite sprite;

    public Settlement() {
        sprite = samples[GameSession.now.rnd.nextInt(samples.length)];
    }

    public void render(Canvas canv, Rect r) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, null);
    }

    public void render(Canvas canv, Rect r,Paint paint) {
        canv.drawBitmap(sprite.bmp, sprite.rect, r, paint);
    }

    public static void init(Sprite[] sprites) {
        samples = sprites;
    }
}
