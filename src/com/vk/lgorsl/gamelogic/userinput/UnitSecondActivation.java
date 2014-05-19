package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.unit.Unit;
import com.vk.lgorsl.gamelogic.world.utils.AlternativeWay;
import com.vk.lgorsl.utils.Touch;

/**
 * "второе нажатие на юнита"
 * при отпускании на клетке с этим юнитом выделение снимается
 * Created by lgor on 04.05.14.
 */
class UnitSecondActivation extends UnitActivation {

    UnitSecondActivation(Gamer gamer) {
        super(gamer);
    }

    public UnitSecondActivation set(Unit unit, AlternativeWay way) {
        this.unit = unit;
        this.way = way;
        setGUIUnit(unit);
        path.clear();
        repaint();
        return this;
    }

    @Override
    State getNext() {
        repaint();
        while (touchesIsEmpty()) {
            if (gamer.session.mustStop || !gameRunning()) {
                return gamer.defaultState;
            }
        }
        Touch t = touches().getTouch();
        if (gamer.gui.interestedInTouch(t)){
            return gamer.gui.set(unit);
        }
        Cell c = getTrueCell(t);
        if (!way.isInto(c) && unit.getCell() != c) {
            return gamer.screenUpdate;
        }
        t = changeFinalWay();
        c = getTrueCell(t);
        if (unit.getCell() == c) {
            return gamer.screenUpdate;
        }
        if (way.isInto(c)) {
            way.getMoveTo(c).apply();
            if (!unit.hasMovementPoints()) {
                setGUIUnit(unit);
                return gamer.checkEndOfTurn;
            }
            way = new AlternativeWay(getMap(), unit);
            repaint();
            return this;
        }
        setGUIUnit(unit);
        return gamer.screenUpdate;
    }
}
