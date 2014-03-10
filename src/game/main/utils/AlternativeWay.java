package game.main.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import game.main.GUI.MapCamera;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Actions.MoveUnit;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;
import game.main.gamelogic.world.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by lgor on 10.03.14.
 */
public class AlternativeWay implements iRenderFeature {

    protected List<Cell> open = new ArrayList<Cell>();
    protected final Map map;
    protected final Unit unit;
    private Paint p = new Paint();
    protected List<Cell> all = new ArrayList<Cell>();
    protected java.util.Map<Cell, Integer> cells = new TreeMap<Cell, Integer>();

    public Action getMoveTo(Cell c) {
        if (!isInto(c)) {
            throw new RuntimeException("Way has't this cell!");
        }
        List<Cell> way = new ArrayList<Cell>();
        while (c != null) {
            way.add(c);
            c = getPrev(c, cells.get(c) + c.getMovindCost());
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
        if (cells.containsKey(c)) {
            return cells.get(c) == val;
        }
        return false;
    }

    public AlternativeWay(Map map, Unit unit) {
        this.map = map;
        this.unit = unit;

        Cell c = unit.getCell();
        add(c.x, c.y, unit.getMovementPoints() + c.getMovindCost());

        while (!open.isEmpty()) {
            c = removeBest();
            int val = cells.get(c);
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
            int v = cells.get(c);
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
        if (cells.containsKey(c) || !c.accessible())
            return;
        int d = val - c.getMovindCost();
        cells.put(c, d);
        if (d > 0) {
            open.add(c);
            all.add(c);
        } else {
            if (c.canMove()) {
                all.add(c);
            }
        }
    }

    public boolean isInto(Cell c) {
        //return all.contains(c);
        return cells.containsKey(c) && c.canMove();
    }

    public void render(MapCamera camera, Canvas canvas) {
        for (Cell c : all) {
            float y = camera.MapToY(c.y);
            float x = camera.MapToX(c.x, c.y);
            float h = camera.getCellHeight();
            float w = camera.getCellWidth();
            canvas.drawLine(x, y + h / 4, x + w / 2, y, p);
            canvas.drawLine(x + w / 2, y, x + w, y + h / 4, p);
            canvas.drawLine(x, y + h / 4, x, y + h * 3 / 4, p);
            canvas.drawLine(x + w, y + h / 4, x + w, y + h * 3 / 4, p);
            canvas.drawLine(x, y + h * 3 / 4, x + w / 2, y + h, p);
            canvas.drawLine(x + w / 2, y + h, x + w, y + h * 3 / 4, p);
        }
    }
}
