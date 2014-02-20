package game.main.gamelogic.world;

import game.main.gamelogic.GameSession;

import java.util.List;
import java.util.Random;

/**
 * Created by lgor on 31.12.13.
 * Класс карты. В перспективе будет туман войны и прочее, для этого в методе getCell будет нужен id игрока
 */
public class Map {

    /*
    Класс карты - массив ячеек, операции по нахождению кратчайших путей и прочего
    клетки массива могут быть null по краям
     */

    //сначала у, потом х
    public final int height, width;
    private final Cell[][] table;

    /**
     * тестовый генератор карты
     * как минимум в будущем надо сделать, чтобы потенциально невидимые клетки (внизу слева и вверху справа) не Cell.empty
     */
    public Map(int width, int height) {
        this.height = height;
        this.width = width;
        table = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                table[y][x] = new Cell(x, y);
            }
        }
    }

    /**
     * заполняет списки всех юнитов и поселений на карте, принадлежащих игроку
     */
    public void listsUnitsSettlements(Player player, List<Unit> units, List<Settlement> settlements) {
        int id = player.id;
        units.clear();
        settlements.clear();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell c = table[j][i];
                if (c != null) {
                    Unit unit = c.getUnit();
                    if (unit != null && unit.playerID == id) {
                        units.add(unit);
                    }
                    if (c.settlement != null && c.settlement.playerID == id) {
                        settlements.add(c.settlement);
                    }
                }
            }
        }
    }

    /**
     * возвращает нормальную клетку или Cell.getEmpty(). В перспективе будет туман войны
     */
    public Cell getCell(int x, int y, int playerId) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return table[y][x];
        }
        return Cell.getEmpty();
    }

    public Cell getCell(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height) {
            return table[y][x];
        }
        return Cell.getEmpty();
    }

    void fillRandom(LandType[] types) {
        Random rnd = GameSession.now.rnd;
        for (Cell[] cc : table) {
            for (Cell c : cc) {
                c.land = types[rnd.nextInt(types.length)];
                if (c.land == types[0] && rnd.nextInt(4) == 0) {
                    c.settlement = new Settlement(c, 0);
                }
            }
        }
    }

    /**
     * кратчайшее расстояние между двумя клетками
     */
    public final int getInterval(Cell c1, Cell c2) {
        return getInterval(c1.x - c2.x, c1.y - c2.y);
    }

    /**
     * расстояние между двумя клетками c разницей в dx, dy по обычным координатам. Метрика хитрая
     * sign(x)==sign(y) -> max(|x|,|y|)
     * else             -> |x|+|y|
     */
    public final int getInterval(int dx, int dy) {
        if (dx >= 0) {
            if (dy >= 0)
                return dx > dy ? dx : dy;
            return dx - dy;
        }
        if (dy >= 0)
            return dy - dx;
        return dx < dy ? -dx : -dy;
    }

    /*
    когда-нибудь многие методы Map станут приватными, а каждому игроку дадут по объекту PlayerMap, который
    будет учитывать туман войны для игрока

    public PlayerMap getMap4Player(Player player){
        return new PlayerMap(player);
    }

    public class PlayerMap{
        private final int id;

        private PlayerMap(Player player){
            id= player.id;
        }

        public Cell getCell(int x,int y){
            return Map.this.getCell(x, y, id);
        }
    }
    */
}