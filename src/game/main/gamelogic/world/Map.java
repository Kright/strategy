package game.main.gamelogic.world;

import game.main.gamelogic.GameSession;
import game.main.utils.CustomRandom;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lgor on 31.12.13.
 * Класс карты.
 * <p/>
 * Оригинальная карта создаётся при помощи MapConstructor - тот обязан передать прямоугольный массив клеток
 * Первоисточник актуальной информации - эта карта.
 * <p/>
 * Карта, которую видит игрок, создаётся при помощи map.createPlayerMap
 * Та карта может отличаться от "правильной" карты - часть клеток может быть невидима или затенена (т.е, находиться в
 * том состоянии, в котором её видели последний раз, и без юнитов)
 */
public class Map implements Iterable<Cell> {

    public interface MapConstructor {
        public int getWidth();

        public int getHeight();

        public Cell getCell(int x, int y);
    }

    public final int width, height;
    /**
     * клетки хранятся хитро, не стоит присваивать значения в table напрямую.
     */
    protected final Cell[][] table;


    public Map(MapConstructor constructor) {
        this.width = constructor.getWidth();
        this.height = constructor.getHeight();
        table = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                table[y][x] = constructor.getCell(x + y / 2, y);
            }
        }
    }

    protected Map(int width, int height) {
        this.height = height;
        this.width = width;
        table = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                table[y][x] = Cell.getEmpty();
            }
        }
    }

    /**
     * заполняет списки всех юнитов и поселений на карте, принадлежащих игроку
     */
    public void listsUnitsSettlements(int id, List<Unit> units, List<Settlement> settlements) {
        units.clear();
        settlements.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell c = table[j][i];
                if (c.hasUnit() && c.getUnit().country.id == id) {
                    units.add(c.getUnit());
                }
                if (c.hasSettlement() && c.getSettlement().country.id == id) {
                    settlements.add(c.getSettlement());
                }
            }
        }
    }

    /**
     * возвращает нормальную клетку, Cell.getEmpty() или затенённую клетку.
     */
    public final Cell getCell(int x, int y) {
        x -= y / 2;
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return table[y][x];
        }
        return Cell.getEmpty();
    }

    /**
     * открывает клетку для карты игрока, делая её актуальной.
     * Для оригинальной карты, конечно, эта функция бесполезна
     */
    public void openCell(int x, int y) {
    }

    /**
     * открывает клетки, соседние с данной
     */
    public void openСellsNear(int x, int y) {
        openCell(x, y);
        openCell(x - 1, y - 1);
        openCell(x - 1, y);
        openCell(x, y - 1);
        openCell(x + 1, y);
        openCell(x, y + 1);
        openCell(x + 1, y + 1);
    }

    /**
     * затеняет клетку.
     * Опять же, для оригинальной карты это бесполезно
     */
    public void shadowCell(int x, int y) {
    }

    public void setUnit(Unit unit, int x, int y) {
        if (!isOnMap(x, y)) return;
        unit.getCell().setUnit(null);       //убираем юнита со старой клетки
        getCell(x, y).setUnit(unit);                 //сажаем его на новую
    }

    public void addSettlement(Settlement settlement, int x, int y) {
        getCell(x, y).setSettlement(settlement);
    }

    /**
     * есть ли клетка с такими координатами в пределах карты
     */
    public final boolean isOnMap(int x, int y) {
        x -= y / 2;
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    /**
     * расстояние между двумя клетками c разницей в dx, dy по обычным координатам. Метрика хитрая
     * sign(x)==sign(y) -> max(|x|,|y|)
     * else             -> |x|+|y|
     */
    public static int getInterval(int dx, int dy) {
        if (dx >= 0) {
            if (dy >= 0)
                return dx > dy ? dx : dy;
            return dx - dy;
        }
        if (dy >= 0)
            return dy - dx;
        return dx < dy ? -dx : -dy;
    }

    /**
     * итератор по всем клеткам карты. Обход - строчками слева направо, сверху вниз
     *
     * @return итератор
     */
    @Override
    public Iterator<Cell> iterator() {
        return new Iterator<Cell>() {
            private int counter = -1;

            @Override
            public boolean hasNext() {
                return counter + 1 < width * height;
            }

            @Override
            public Cell next() {
                counter++;
                return table[counter / width][counter % width];
            }

            @Override
            public void remove() {
            }
        };
    }

    public static MapConstructor getTestConstructor(final int width, final int height, final List<LandType> types) {
        return new MapConstructor() {
            CustomRandom rnd = GameSession.now.rnd;

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public Cell getCell(int x, int y) {
                LandType type = types.get(rnd.get(types.size()));
                return new Cell(x, y, type, type.nextLayer());
            }
        };
    }

    /**
     * методы типа создания юнита и поселения делегируются оригинальной карте, а потом эти изменения, возможно, станут
     * видны на остальных картах, если рядом есть наблюдатель (юнит или город данной нации)
     *
     * @return персонализированную карту мира для игрока
     */
    public Map createPlayerMap() {
        return new Map(width, height) {
            @Override
            public void setUnit(Unit unit, int x, int y) {
                Map.this.setUnit(unit, x, y);
            }

            @Override
            public void addSettlement(Settlement settlement, int x, int y) {
                Map.this.addSettlement(settlement, x, y);
            }

            @Override
            public void listsUnitsSettlements(int id, List<Unit> units, List<Settlement> settlements) {
                Map.this.listsUnitsSettlements(id, units, settlements);
            }

            @Override
            public void openCell(int x, int y) {
                if (isOnMap(x, y)) {
                    x-=y/2;
                    table[y][x] = Map.this.table[y][x];
                }
            }

            @Override
            public void shadowCell(int x, int y) {
                if (isOnMap(x, y)) {
                    x-=y/2;
                    table[y][x] = table[y][x].getShadowded();
                }
            }
        };
    }

}
