package game.main.gamelogic.world;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import game.main.GUI.iRender;

/*
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
    private boolean road = false;

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
    }

    protected int getPlayerID() {
        return 0; //заглушка
    }

    public Unit getUnit() {
        return unit;
    }

    public boolean hasUnit() {
        return unit != null;
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

    public boolean hasRoad() {
        return road;
    }

    public void setRoad(boolean hasRoad) {
        road = hasRoad;
    }

    /**
     * отнимаемые очки перемещения
     */
    public int getMovindCost() {
        //TODO движение по дорогам
        if (road)
            return 1;
        return land.movingCost;
    }

    /**
     * можно ли переместиться на эту клетку прямо сейчас (На ней нет юнитов и по ней можно ходить)
     */
    public boolean canMove() {
        //TODO
        return accessible() && !hasUnit();
    }

    /**
     * проходима ли клетка в принципе (т.е, не гора и не река)
     */
    public boolean accessible() {
        //TODO
        return true;
    }

    /**
     * принадлежит ли эта клетка карте
     */
    public boolean isNull() {
        return false;
    }

    public static Cell getEmpty() {
        return empty;
    }

    static private Cell empty = new Cell(-1, -1) {
        @Override
        public void render(Canvas canv, Rect cell) {
            /*
            ничего. Это же пустая клетка.
            Хотя, возможно, лучше рисовать что-нибудь чёрное
             */
        }

        @Override
        public void render(Canvas canv, Rect cell, Paint paint) {
        }

        @Override
        protected int getPlayerID() {
            return 0;
        }

        @Override
        public boolean accessible() {
            return false;
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public int getMovindCost() {
            return 0;
        }

        @Override
        public boolean hasUnit() {
            return false;
        }

        @Override
        public void setUnit(Unit unit) {
            //ничего. Нефиг сюда ходить
        }
    };
}