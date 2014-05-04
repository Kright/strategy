package com.vk.lgorsl.gamelogic.userinput;

import android.graphics.Canvas;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.MapRender;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.Unit;
import com.vk.lgorsl.gamelogic.world.utils.AlternativeWay;
import com.vk.lgorsl.utils.Touch;

import java.util.ArrayList;

/**
 * абстрактный класс для удобства
 * Created by lgor on 04.05.14.
 */
abstract class UnitActivation extends State {

    protected Unit unit;
    protected AlternativeWay way;
    protected ArrayList<Cell> path = new ArrayList<Cell>();

    UnitActivation(Gamer gamer) {
        super(gamer);
    }

    protected Touch changeFinalWay() {
        Cell c1 = unit.getCell();
        Cell c2;
        Touch t;
        while (true) {
            if (gamer.session.touchBuffer.isEmpty()) {
                gamer.session.repaint();
            }
            while (gamer.session.touchBuffer.isEmpty()) {
                GameSession.sleep(20);
            }
            t = gamer.session.touchBuffer.getTouch();
            c2 = getTrueCell(t);
            if (c1 != c2) {
                path.clear();
                c1 = c2;
                if (way.isInto(c2)) {
                    way.getWayTo(path, c2);
                }
            }
            if (t.lastTouch()) {
                path.clear();
                return t;
            }
        }
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        MapCamera.CellIterator iterator = gamer.camera.initRender(canvas, gamer.country.map);
        gamer.camera.drawLands(gamer.country.map, iterator);
        way.render(gamer.camera, canvas);
        gamer.camera.renderPath(path);
        gamer.camera.drawUnits(iterator);
    }
}
