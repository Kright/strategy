package game.main.gamelogic.userinput;

import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.gamelogic.world.utils.AlternativeWay;
import game.main.utils.Touch;

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
        path.clear();
        return this;
    }

    @Override
    State getNext() {
        while (gamer.session.touchBuffer.isEmpty()) {
            if (gamer.session.mustStop || !gamer.session.running) {
                return gamer.defaultState;
            }
        }
        Touch t = gamer.session.touchBuffer.getTouch();
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
                return gamer.screenUpdate;
            }
            way = new AlternativeWay(gamer.country.map.getTrueMap(), unit);
            gamer.session.repaint();
            return this;
        }
        return gamer.screenUpdate;
    }
}
