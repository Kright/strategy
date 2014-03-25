package game.main.utils;

import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael-PC on 25.03.14.
 */
public class LongWay {
    protected final Map map;
    protected java.util.List<CellWay> closed = new ArrayList<CellWay>();
    protected java.util.List<CellWay> open = new ArrayList<CellWay>();
    protected List<Cell> path = new ArrayList<Cell>();
    protected final Cell target;

    public LongWay(Map map, Cell A, Cell B) {
        this.map = map;
        target=B;
        open.add(new CellWay(A,null,hDistance(A,target)));
        CellWay parent;
        while(!open.isEmpty()|| contain(target)){
            parent=getMinOpen();
            addOpen(parent, 0, 1);
            addOpen(parent, 1, 1);
            addOpen(parent, 1, 0);
            addOpen(parent, -1, 0);
            addOpen(parent, -1, -1);
            addOpen(parent, 0, -1);
            addClose(parent);
        }
        CellWay pathMember=open.get(indexOf(target));
        while(pathMember.parent!=null){
            path.add(pathMember.c);
            pathMember=pathMember.parent;
        }
        Collections.reverse(path);

    }

    private CellWay getMinOpen(){
        int minf=-1;
        CellWay p=null;
        for(CellWay cW:open){
            if(((minf)==-1) || (cW.f()<minf)){
                p=cW;
                minf=cW.f();
            }
        }
        return p;

    }

    private void addOpen(CellWay p,int dx ,int dy){
        Cell c=map.getCell(p.c.x+dx,p.c.y+dy);
        if (contain(c) || !c.accessible())
            return;
        open.add(new CellWay(c,p,hDistance(c,target)));
    }

    private void addClose(CellWay p){
        open.remove(p);
        closed.add(p);
    }


    private int indexOf(Cell c){
        for(CellWay cW:open){
            if(cW.c==c) return open.indexOf(cW);
        }
        return -1;
    }

    private boolean contain(Cell c){
        if(indexOf(c)==-1) return false;
        return true;
    }

    int hDistance(Cell c1, Cell c2) { // эвристическая функция расстояния
        return Map.getInterval(c1.x - c2.x, c1.y - c2.y)+c1.getMovindCost();
    }

    public List<Cell> getPath(){
        return path;
    }


    private static class CellWay {
        Cell c;
        CellWay parent;
        private int way=0;
        private int h;

        CellWay(Cell c, CellWay parent, int hDist) {
            this.c = c;
            this.parent = parent;
            h=hDist;
            if(parent!=null)
                way = parent.way + c.getMovindCost();
        }
        int f(){
            return h+way;
        }
    }
}

