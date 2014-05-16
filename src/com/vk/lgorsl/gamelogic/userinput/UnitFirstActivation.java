package com.vk.lgorsl.gamelogic.userinput;

import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.Unit;
import com.vk.lgorsl.gamelogic.world.utils.AlternativeWay;
import com.vk.lgorsl.utils.Touch;

/**
 * первое нажатие на юнита.
 * Если отпустить на нём же, перейдёт к "второму нажатию на юнита"
 * если отпустить в пределах перемещения юнита - переместит его
 * если отпустить в нереальном месте - сбрасывается.
 * Created by lgor on 04.05.14.
 */
class UnitFirstActivation extends UnitActivation {

    UnitFirstActivation(Gamer gamer) {
        super(gamer);
    }

    public UnitFirstActivation setUnit(Unit unit) {
        this.unit = unit;
        way = new AlternativeWay(getMap(), unit);
        return this;
    }

    @Override
    public State getNext() {
        repaint();
        Touch t = changeFinalWay();
        Cell c = getTrueCell(t);
        if (way.isInto(c)) {
            way.getMoveTo(c).apply();
            return gamer.checkEndOfTurn;
        }
        if (unit.getCell() == c) {
            gamer.session.repaint();
            return gamer.unitSecondActivation.set(unit, way);
        }
        return gamer.checkEndOfTurn;
    }
}
