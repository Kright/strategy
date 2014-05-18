package com.vk.lgorsl.gamelogic.userinput;

import android.graphics.Canvas;
import com.vk.lgorsl.GUI.BorderLine;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.MapRender;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.Unit;

import java.util.ArrayList;

/**
 * выделение юнита, который не может подвинуться
 * Created by lgor on 16.05.14.
 */
class UnmovingUnitActivation extends State{

    private final BorderLine borderLine= new BorderLine();
    private final ArrayList<Cell> cells = new ArrayList<Cell>();

    UnmovingUnitActivation(Gamer gamer) {
        super(gamer);
    }

    public UnmovingUnitActivation setUnit(Unit unit){
        cells.clear();
        cells.add(unit.getCell());
        borderLine.init(cells);
        borderLine.setColorNum(unit.country.id);
        return this;
    }

    @Override
    State getNext() {
        while (!touches().getTouch().lastTouch()){
            if (!gameRunning()){
                return gamer.screenUpdate;
            }
            repaint();
        }
        while (touchesIsEmpty()) {
            if (!gameRunning()) {
                return gamer.screenUpdate;
            }
            GameSession.sleep(20);
        }
        return gamer.defaultState;
    }

    @Override
    void paint(Canvas canvas, MapRender render) {
        MapCamera.CellIterator iterator = render.initRender(canvas, gamer.country.map);
        render.drawLands(gamer.country.map, iterator);
        borderLine.render(render, canvas);
        render.drawUnits(iterator);
        render.drawGUI();
    }
}
