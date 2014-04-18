package game.main.gamelogic.userinput;

import android.graphics.Canvas;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.AlternativeWay;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.utils.Touch;

/**
 * повторное нажатие на активированного юнита.
 * Если нажать на доступное для движения поле - ходит, и если может ходить, остаётся выделенным.
 * иначе снимается выделение с юнита
 * Created by lgor on 18.04.14.
 */
class UnitSecondActivation extends Gamer.State{

    public UnitSecondActivation(Gamer gamer){
        gamer.super();
    }

    private Unit unit;
    private AlternativeWay way;

    public UnitSecondActivation set(Unit unit, AlternativeWay way){
        this.unit = unit;
        this.way = way;
        return this;
    }

    @Override
    public Gamer.State getNext() {
        Touch t;
        while (!(t = waitTouch()).firstTouch()) ;       //waiting first touch
        Cell c = getTrueCell(t);
        if (!way.isInto(c) && unit.getCell() != c){
            return gamer().defaultState.mustUpdate();
        }
        while (!(t = waitTouch()).lastTouch()) ;        //waiting last touch
        c = getTrueCell(t);
        if (unit.getCell() == c){
            return gamer().defaultState.mustUpdate();
        }
        if (way.isInto(c)){
            way.getMoveTo(c).apply();
            if (!unit.hasMovementPoints()){
                return gamer().defaultState.mustUpdate();
            }
            way = new AlternativeWay(gamer().country.map.getTrueMap(), unit);
            repaint();
            return this;
        }
        return gamer().defaultState.mustUpdate();
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        camera().render(canvas, gamer().country.map, way);
    }
}
