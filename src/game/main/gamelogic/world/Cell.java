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
public class Cell implements iRender, Comparable<Cell> {
    /*
    клетка карты, содержит всякую информацию - тип ландшафта, юнита, если он есть, улучшения и т.п.
    */
    public final int x, y;
    public final boolean shadowded;
    public LandType land;
    Settlement settlement = null;
    private Unit unit = null;
    private boolean road = false;

    Cell(int x, int y, LandType land) {
        this.x = x;
        this.y = y;
        this.land = land;
        shadowded = false;
    }

    /**
     * затенённая копия клетки
     */
    private Cell(Cell c){
        x=c.x;
        y=c.y;
        shadowded=true;
        land=c.land;
        settlement=c.settlement;
        road=c.road;
    }

    /**
     * @return затенённую клетку. та не меняется со временем и на ней не видны юниты.
     */
    public Cell getShadowded(){
        return shadowded?this:new Cell(this);
    }

    @Override
    public void render(Canvas canv, Rect cell, Paint paint) {
        land.render(canv, cell, paint);
        if (settlement != null)
            settlement.render(canv, cell, paint);
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
     *
     * @param unit may be null
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) return false;
        Cell c = (Cell) o;
        return c.x == x && c.y == y;
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
     * может ли Unit переместиться на эту клетку прямо сейчас (На ней нет юнитов, вражеских поселений и можно ходить)
     */
    public boolean canMove(Unit unit) {
        return accessible() && !hasUnit() && (settlement == null || settlement.country != unit.country);
    }

    /**
     * проходима ли клетка в принципе (т.е, не гора и не река)
     */
    public boolean accessible() {
        return land.accessable;
    }

    /**
     * принадлежит ли эта клетка карте
     */
    public boolean isNull() {
        return false;
    }

    /**
     * @return есть ли поселение на этой клетке
     */
    public boolean hasSettlement() {
        return settlement != null;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public static Cell getEmpty() {
        return empty;
    }

    static private Cell empty = new Cell(-1, -1, null) {
        @Override
        public void render(Canvas canv, Rect cell, Paint paint) {
            /*
            ничего. Это же пустая клетка.
            Хотя, возможно, лучше рисовать что-нибудь чёрное
             */
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
        public Cell getShadowded() {
            return this;    //теоретически, метод никогда не будет вызван, но - реализованное поведение вполне логично.
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

        @Override
        public boolean equals(Object o) {
            return false;                   //пустая клетка не равна другой пустой клетке
        }
    };

    @Override
    public int compareTo(Cell another) {
        if (y != another.y) {
            return y - another.y;
        }
        return x - another.x;
    }
}