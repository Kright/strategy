package game.main.gamelogic.userinput;

import android.graphics.Canvas;
import game.main.GUI.MapCamera;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.AlternativeWay;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый класс для состояния с активным юнитом
 * В частности, здесь функции обновления пути и его рисования
 * Created by lgor on 19.04.14.
 */
abstract class UnitActivation extends Gamer.State {

    protected Unit unit;
    protected AlternativeWay way;
    protected ArrayList<Cell> path;

    public UnitActivation(Gamer gamer) {
        gamer.super();
        path = new ArrayList<Cell>();
    }

    protected Touch changeFinalWay() {
        Cell c1 = unit.getCell();
        Cell c2;
        Touch t;
        while (!(t = waitTouch()).lastTouch()) {
            c2 = getTrueCell(t);
            if (c1 != c2) {
                mayBeRepaint(); //при вызове waitTouch будет вызван repaint(), если не будет новых событий нажатия
                path.clear();
                c1 = c2;
                if (way.isInto(c2)) {
                    way.getWayTo(path, c2);
                }
            }
        }
        path.clear();
        return t;
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        MapCamera.CellIterator iterator = camera().initRender(canvas, getMap());
        camera().drawLands(getMap(), iterator);
        way.render(camera(), canvas);
        camera().renderPath(path);
        camera().drawUnits(iterator);
    }
}
