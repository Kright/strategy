package game.main.gamelogic.world;

import java.util.*;

/**
 * Created by Michael-PC on 25.03.14.
 */
public class LongWay {
    protected final Map map;
    protected TreeSet<CellWay> open = new TreeSet<CellWay>();
    protected HashSet<Cell> openCell = new HashSet<Cell>();
    protected HashSet<Cell> closeCell = new HashSet<Cell>();
    protected List<Cell> path = new ArrayList<Cell>();
    protected final Cell target;
    protected CellWay cWTarget;
    protected boolean targetContained;

    public LongWay(Map map, Cell A, Cell B) {
        this.map = map;
        target = B;
        open.add(new CellWay(A, null, hDistance(A, target), B.x - A.x, B.y - B.y));
        openCell.add(A);
        targetContained = false;

        CellWay parent;
        while (!open.isEmpty() && !targetContained) {
            parent = getMinOpen();
            addOpen(parent, 0, 1);
            addOpen(parent, 1, 1);
            addOpen(parent, 1, 0);
            addOpen(parent, -1, 0);
            addOpen(parent, -1, -1);
            addOpen(parent, 0, -1);
            addClose(parent);
        }
        CellWay pathMember = cWTarget;
        while (pathMember.parent != null) {
            path.add(pathMember.c);
            pathMember = pathMember.parent;
        }
        path.add(pathMember.c);
        Collections.reverse(path);
    }

    private CellWay getMinOpen() {
        return open.first();

    }

    private void addOpen(CellWay p, int dx, int dy) {
        Cell c = map.getCell(p.c.x + dx, p.c.y + dy);
        if (contain(c) || !c.accessible() || containClose(c))
            return;
        CellWay cW = new CellWay(c, p, hDistance(c, target), target.x - c.x, target.y - c.y);

        open.add(cW);

        openCell.add(c);

        if (c == target) {
            targetContained = true;
            cWTarget = cW;
        }
    }

    private void addClose(CellWay p) {

        open.remove(p);
        openCell.remove(p.c);
        closeCell.add(p.c);
    }


    private boolean contain(Cell c) {
        return openCell.contains(c);
    }

    private boolean containClose(Cell c) {

        return closeCell.contains(c);
    }


    int hDistance(Cell c1, Cell c2) { // эвристическая функция расстояния
        return (int) (2 * Map.getInterval(c1.x - c2.x, c1.y - c2.y)) + 2 * c1.getMovindCost();
    }


    public List<Cell> getPath() {
        return path;
    }


    private static class CellWay implements Comparable {
        Cell c;
        CellWay parent;
        private int way = 0;
        private int h;
        int signx;
        int signy;

        CellWay(Cell c, CellWay parent, int hDist, int signx, int signy) {
            this.c = c;
            this.parent = parent;
            h = hDist;
            if (parent != null)
                way = parent.way + c.getMovindCost();
            if (signx >= 0) this.signx = 1;
            else this.signx = -1;
            if (signy >= 0) this.signy = 1;
            else this.signy = -1;
        }

        int f() {
            return h + way;
        }

        public int compareTo(Object obj) {
            CellWay T = (CellWay) obj;
            if (T.f() != f()) return f() - T.f();
            if (T.c.x != c.x) return (c.x - T.c.x);
            if (T.c.y != c.y) return (c.y - T.c.y);
            return 0;

        }

    }

}

