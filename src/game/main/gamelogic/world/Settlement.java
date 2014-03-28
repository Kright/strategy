package game.main.gamelogic.world;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.utils.Sprite;
import game.main.utils.SpriteBank;

/**
 * поселение
 * Наследуются - замок, деревушка, город
 * Created by lgor on 25.01.14.
 */
public abstract class Settlement implements iRender {

    public static Sprite shadow;

    protected Country country;
    public final Cell cell;

    public Settlement(Country country, Cell cell) {
        this.country = country;
        this.cell = cell;
    }

    /**
     * called when new turn starts
     * @return taxes from this settlement, may be positive or 0 or negative (if unprofitable)
     */
    public abstract int getTaxes();

    public abstract void nextTurn();

    public abstract void render(Canvas canv, Rect r, Paint paint);

    public static void init(SpriteBank sprites) {
        Village.sprite = sprites.get("village");
        Castle.sprite = sprites.get("castle");
        shadow = sprites.get("shadow");
    }
}
