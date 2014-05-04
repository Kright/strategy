package game.main.gamelogic.userinput;

import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.gamelogic.world.utils.AlternativeWay;
import game.main.utils.Touch;

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
        way = new AlternativeWay(gamer.country.map.getTrueMap(), unit);
        return this;
    }

    @Override
    public State getNext() {
        gamer.session.repaint();
        Touch t = changeFinalWay();
        Cell c = getTrueCell(t);
        if (way.isInto(c)) {
            way.getMoveTo(c).apply();
            return gamer.screenUpdate;
        }
        if (unit.getCell() == c) {
            gamer.session.repaint();
            return gamer.unitSecondActivation.set(unit, way);
        }
        return gamer.screenUpdate;
    }
}
