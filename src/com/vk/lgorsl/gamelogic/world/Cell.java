package com.vk.lgorsl.gamelogic.world;


import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.iRender;

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
    public iRender nextRender;
    private Settlement settlement = null;
    private Unit unit = null;
    private boolean road = false;
    private Castle controlledByCastle = null;
    private LandUpgrade landUpgrade;

    /**
     * почему-то вариант с со строчкой nextRender = land.nextRender выдаёт ошибку java.lang.ExceptionInInitializerError
     * потому nextRender передаётся вручную
     */

    Cell(int x, int y, LandType land, iRender nextRender) {
        this.x = x;
        this.y = y;
        this.land = land;
        shadowded = false;
        this.nextRender = nextRender;
    }

    /**
     * используется только внутеннее клеткой
     */
    private Cell(int x, int y, LandType land) {
        this.x = x;
        this.y = y;
        this.land = land;
        shadowded = false;
        this.nextRender = iRender.NullRender.get();
    }

    /**
     * затенённая копия клетки
     */
    private Cell(Cell c) {
        x = c.x;
        y = c.y;
        shadowded = true;
        land = c.land;
        settlement = c.settlement;
        road = c.road;
        nextRender = c.nextRender;
    }

    /**
     * @return затенённую клетку. та не меняется со временем и на ней не видны юниты.
     */
    public Cell getShadowded() {
        return shadowded ? this : new Cell(this);
    }

    @Override
    public void render(RenderParams params) {
        land.render(params);
    }

    public void setLandUpgrade(LandUpgrade landUpgrade) {
        this.landUpgrade = landUpgrade;
        if (landUpgrade != null) {
            this.nextRender = landUpgrade;
        } else {
            nextRender = land.nextLayer();
        }
    }

    public boolean hasLandUpgrade() {
        return (landUpgrade != null);
    }

    /**
     * @return крепость, которая контролирует эту клетку
     */
    public Castle controlledByCastle() {
        return controlledByCastle;
    }

    public void setCastleControl(Castle castle) {
        controlledByCastle = castle;
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
        return road || hasSettlement();
    }

    public void setRoad(boolean hasRoad) {
        road = hasRoad;
    }

    /**
     * отнимаемые очки перемещения
     */
    public int getMovindCost() {
        //TODO движение по дорогам
        if (road || hasSettlement())
            return 1;
        return land.movingCost;
    }

    /**
     * может ли Unit переместиться на эту клетку прямо сейчас (На ней нет юнитов, вражеских поселений и можно ходить)
     */
    public boolean canMove(Unit unit) {
        return accessible() && !hasUnit() && (settlement == null ||
                settlement.country == unit.country || !(settlement instanceof Castle));
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

    public String toString() {
        return "" + x + " " + y;
    }


    @Override
    public int compareTo(Cell another) {
        if (y != another.y) {
            return y - another.y;
        }
        return x - another.x;
    }

    /**
     * две разные клетки с одинаковыми координатами считаюстя эквивалентными
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) return false;
        Cell c = (Cell) o;
        return c.x == x && c.y == y;
    }

    @Override
    public int hashCode() {
        return (y << 20) | (x << 8);
    }

    /**
     * @return есть ли поселение на этой клетке
     */
    public boolean hasSettlement() {
        return settlement != null;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
        nextRender = settlement == null ? land.nextLayer() : settlement;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public static Cell getEmpty() {
        return empty;
    }

    static private Cell empty = new Cell(-1, -1, null) {
        @Override
        public void render(RenderParams params) {
            /*
            ничего. Это же пустая клетка.
            Хотя, возможно, лучше рисовать что-нибудь чёрное
             */
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
        public void setUnit(Unit unit) {
            //ничего. Нефиг сюда ходить
        }

        @Override
        public void setCastleControl(Castle castle) {
            //эту клетку нельзя контролировать
        }

        @Override
        public void setSettlement(Settlement settlement) {
            //и строить на ней тоже нельзя
        }

        @Override
        public boolean equals(Object o) {
            return false;                   //пустая клетка не равна другой пустой клетке
        }

        @Override
        public void setLandUpgrade(LandUpgrade landUpgrade) {
            //никаких апгрейдов
        }

        @Override
        public boolean hasLandUpgrade() {
            return false;
        }
    };
}