package game.main.gamelogic.userinput;

import game.main.gamelogic.world.AlternativeWay;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.utils.Touch;

/**
 * первое нажатие на юнита выделяет его.
 * Если отпустить нажатие на оступном поле - ещё и ходит
 * Created by lgor on 18.04.14.
 */
class UnitFirstActivation extends UnitActivation {

    public UnitFirstActivation(Gamer gamer) {
        super(gamer);
    }

    public UnitFirstActivation setUnit(Unit unit) {
        this.unit = unit;
        way = new AlternativeWay(gamer().country.map.getTrueMap(), unit);
        return this;
    }

    @Override
    public Gamer.State getNext() {
        repaint();
        Touch t = changeFinalWay();
        Cell c = getTrueCell(t);
        if (way.isInto(c)) {
            way.getMoveTo(c).apply();
            return gamer().defaultState.mustUpdate();
        }
        if (unit.getCell() == c) {
            return gamer().unitSecondActivation.set(unit, way);
        }
        return gamer().defaultState.mustUpdate();
    }
}
