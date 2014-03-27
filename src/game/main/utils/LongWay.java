package game.main.utils;


import android.util.Log;

import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Map;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

/**
 * Created by Michael-PC on 25.03.14.
 *
 * некоторые предложения по оптимизации (lgor):
 * можно вместо линейного поиска по "open" сделать, например, SortedSet, аккуратно ввести операцию сравнения,
 * и в getMinOpen просто брать самый первый элемент.
 *
 * Кроме того, часто вызывается contain(), которая работает за O(n), наверно, лучше ввести коллекцию Cell на основе
 * дерева или хеш-таблиц и проверять наличие в ней за O(log(n)), аналогично с containClose()
 *
 * в цикле while( ... contain(target)) лучше смотреть, не добавили ли мы такую клетку в момент добавления.
 * например, можно было бы поставить проверку в addOpen и возврашать с!=target, а в цикле while(){...
 * if (addOpen(...) ||
 *     addOpen(...) ||
 *                  ...) then { pathMember = ... ; break; }
 *
 */
public class LongWay  {
    protected final Map map;
    protected java.util.List<CellWay> closed = new ArrayList<CellWay>();
    protected java.util.List<CellWay> open = new ArrayList<CellWay>();
    protected List<Cell> path = new ArrayList<Cell>();
    protected final Cell target;

    public LongWay(Map map, Cell A, Cell B) {
        this.map = map;
        target=B;
        open.add(new CellWay(A,null,hDistance(A,target)));
        CellWay parent=null;
        while(!open.isEmpty()&& !contain(target)){
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
        path.add(pathMember.c);
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
        if (contain(c) || !c.accessible()||containClose(c))
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
        for(CellWay cW:open){
            if(cW.c==c) return true;
        }
        return false;
    }
    private int closeIndexOf(Cell c){
        for(CellWay cW:closed){
            if(cW.c==c) return closed.indexOf(cW);
        }
        return -1;
    }

    private boolean containClose(Cell c){
        for(CellWay cW:closed){
            if(cW.c==c) return true;
        }
        return false;
    }

    /**
     * эвристическая функция расстояния
     * бонус за уменьшение интервала не стоит делать слишком большим (больше, чем минимальная стоимость перемещения)
     */
    int hDistance(Cell c1, Cell c2) {
        return (int)(3*Map.getInterval(c1.x - c2.x, c1.y - c2.y))+3*c1.getMovindCost();
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

