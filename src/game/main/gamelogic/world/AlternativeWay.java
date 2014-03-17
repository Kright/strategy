package game.main.gamelogic.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * множество возможных перемещнию юнита, выдаёт Action перемещения в конкретную клетку
 * Created by lgor on 10.03.14.
 */
public class AlternativeWay extends Region {

    protected List<Cell> open = new ArrayList<Cell>();
    protected final Map map;
    protected final Unit unit;

    protected java.util.Map<Cell, Integer> cellsMap = new TreeMap<Cell, Integer>();

    public Action getMoveTo(Cell c) {
        if (!isInto(c)) {
            throw new IllegalArgumentException("Way hasn't this cell!");
        }
        List<Cell> way = new ArrayList<Cell>();
        while (c != null) {
            way.add(c);
            c = getPrev(c, cellsMap.get(c) + c.getMovindCost());
        }
        Collections.reverse(way);
        return new MoveUnit(unit, way);
    }

    private Cell getPrev(Cell c, int goal) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != -j) {     //if interval is 1
                    if (isVal(c.x + i, c.y + j, goal)) {
                        return map.getCell(c.x + i, c.y + j, goal);
                    }
                }
            }
        }
        return null;
    }

    private boolean isVal(int x, int y, int val) {
        Cell c = map.getCell(x, y);
        if (cellsMap.containsKey(c)) {
            return cellsMap.get(c) == val;
        }
        return false;
    }

    public AlternativeWay(Map map, Unit unit) {
        super(new ArrayList<Cell>(24));
        this.map = map;
        this.unit = unit;

        Cell c = unit.getCell();
        add(c.x, c.y, unit.getMovementPoints() + c.getMovindCost());

        while (!open.isEmpty()) {
            c = removeBest();
            int val = cellsMap.get(c);
            add(c.x + 1, c.y, val);
            add(c.x - 1, c.y, val);
            add(c.x, c.y + 1, val);
            add(c.x, c.y - 1, val);
            add(c.x + 1, c.y + 1, val);
            add(c.x - 1, c.y - 1, val);
        }
    }

    private Cell removeBest() {
        int a = -1;
        Cell result = null;
        for (Cell c : open) {
            int v = cellsMap.get(c);
            if (v > a) {
                result = c;
                a = v;
            }
        }
        open.remove(result);
        return result;
    }

    private void add(int x, int y, int val) {
        Cell c = map.getCell(x, y);
        if (cellsMap.containsKey(c) || !c.accessible())
            return;
        int d = val - c.getMovindCost();
        cellsMap.put(c, d);
        if (d > 0) {
            open.add(c);
            cells.add(c);
        } else {
            if (c.canMove()) {
                cells.add(c);
            }
        }
    }

    @Override
    public boolean isInto(Cell c) {
        return cellsMap.containsKey(c) && c.canMove();
    }
}
