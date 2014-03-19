package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.utils.Sprite;

/**
 * Created by lgor on 31.12.13.
 */
public class LandType implements iRender {
    /*
    тип ландшафта
        его параметры, возможные улучшения, возможно картинка, но хочется несколько для разнообразия
    */

    String name;
    public final int movingCost;  //заглушка
    public final boolean accessable;
    protected final Sprite sprite;

    public LandType(Sprite sprite, int movingCost, String name) {
        this.sprite = sprite;
        this.name = name;
        this.movingCost = movingCost;
        this.accessable = (movingCost>0);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint p) {
        canv.drawBitmap(sprite.bmp, sprite.rect, cell, p);
    }
}
