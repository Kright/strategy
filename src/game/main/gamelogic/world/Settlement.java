package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.utils.Sprite;

/**
 * поселение
 * Наследуются - замок, деревушка, город
 * Created by lgor on 25.01.14.
 */
public abstract class Settlement implements iRender {

    protected Country country;

    protected Cell cell;

    public Settlement(Country country, Cell cell) {
        this.country = country;
        this.cell = cell;
    }

    /**
     * called when new turn starts
     * @return taxes from this settlement, may be positive or 0 or negative (if unprofitable)
     */
    public int getTaxes(){
        return  0;
    }

    public abstract void nextTurn();

    public abstract void render(Canvas canv, Rect r, Paint paint);

    public static void init(Sprite[] sprites) {
        Village.sprite = sprites[0];
        Castle.sprite = sprites[1];
    }
}
