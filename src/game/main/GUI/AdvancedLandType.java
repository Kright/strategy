package game.main.GUI;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.gamelogic.world.LandType;
import game.main.utils.Sprite;

/**
 * Created by lgor on 09.03.14.
 */
public class AdvancedLandType extends LandType {

    private final float yC, xC;  //поправки по x, y;

    private Rect rect = new Rect();

    /**
     * xC, yС - поправочные коэффициенты.
     * например, если картинка на половину высоты заходит сверху и на четверть ширине выглядывает ячейки,
     * yC = (-0.5), xC=0.25;
     */
    public AdvancedLandType(Sprite sprite, int movingCost, float xC, float yC, String name) {
        super(sprite, movingCost, name);
        this.xC = xC;
        this.yC = yC;
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint p) {
        rect.set(cell.left, cell.top + (int) (yC * cell.height()), cell.right + (int) (xC * cell.width()), cell.bottom);
        canv.drawBitmap(sprite.bmp, sprite.rect, rect, p);
    }
}
