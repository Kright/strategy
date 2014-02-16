package game.main.gamelogic;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;
import game.main.gamelogic.meta.LandType;
import game.main.gamelogic.meta.Settlement;

/**
 * Created by lgor on 31.12.13.
 * Клетка карты.
 * Находящиеся на ней юнит и поселение должны иметь ссылку на неё - потому что только клетка знает свои координаты.
 */
public class Cell implements iRender {
    /*
клетка карты, содержит всякую информацию - тип ландшафта, юнита, если он есть, улучшения и т.п.
*/
    public final int x, y;
    public LandType land;
    Settlement settlement = null;
    private Unit unit = null;
    //boolean road = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(Canvas canv, Rect cell) {
        render(canv, cell, null);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint paint) {
        canv.drawBitmap(land.sprite.bmp, land.sprite.rect, cell, paint);
        if (settlement != null)
            settlement.render(canv, cell);
        if (unit != null) {
            unit.render(canv, cell);
        }
    }

    protected int getPlayerID() {
        return 0; //заглушка
    }

    public Unit getUnit() {
        return unit;
    }

    /**
     * записываем, что юнит в нашей клетке, и ещё обновляем ссылку на клетку в юните
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
        if (unit != null) {
            unit.setCell(this);
        }
    }

    /**
     * отнимаемые очки перемещения
     */
    public int getMovindCost() {
        //TODO движение по дорогам
        return land.movingCost;
    }

    /**
     * можно ди переместиться на эту клетку прямо сейчас (На ней нет юнитов и по ней можно ходить)
     */
    public boolean canMove() {
        //TODO
        return accessible();
    }

    /**
     * проходима ли клетка в принципе (т.е, не гора и не река)
     */
    public boolean accessible() {
        //TODO
        return true;
    }
}