package game.main.gamelogic.userinput;

import android.graphics.Canvas;
import game.main.gamelogic.MapRender;
import game.main.gamelogic.world.AlternativeWay;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Unit;
import game.main.utils.Touch;

/**
 * первое нажатие на юнита выделяет его.
 * Если отпустить нажатие на оступном поле - ещё и ходит
 * Created by lgor on 18.04.14.
 */
class UnitActivation extends Gamer.State{

    public UnitActivation(Gamer gamer){
        gamer.super();
    }

    private Unit unit;
    private AlternativeWay way;

    public UnitActivation setUnit(Unit unit){
        this.unit = unit;
        way = new AlternativeWay(gamer().country.map.getTrueMap(), unit);
        return this;
    }

    @Override
    public Gamer.State getNext() {
        repaint();
        Touch t;
        while(!(t = waitTouch()).lastTouch()) ; //waiting last touch
        Cell c = getTrueCell(t);
        if (way.isInto(c)){
            way.getMoveTo(c).apply();
            return gamer().defaultState.mustUpdate();
        }
        if (unit.getCell() == c){
            return gamer().unitSecondActivation.set(unit, way);
        }
        return gamer().defaultState.mustUpdate();
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        render.render(canvas, gamer().country.map, way);
    }
}
