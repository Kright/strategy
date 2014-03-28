package game.main.gamelogic;

import game.main.GUI.MapCamera;
import game.main.GUI.UnitSelection;
import game.main.gamelogic.world.*;
import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Player;
import game.main.gamelogic.world.World;
import game.main.gamelogic.world.AlternativeWay;
import game.main.utils.Touch;

import java.util.List;

/**
 * Класс игрока, который сидит и тыкает в экран пальцем.
 * Created by lgor on 09.02.14.
 */
public class Gamer extends Player {

    private AlternativeWay way = null;
    private boolean nextTurnReady = false;

    public Gamer(World world, Country country) {
        super(world, country);
    }

    @Override
    public boolean update(MapCamera camera, List<Touch> touches) {
        for (Touch t : touches) {
            update(camera, t);
        }
        return !nextTurnReady;
    }

    private void update(MapCamera camera, Touch touch) {
        if (touch.count() == 1) {
            camera.move(-touch.dx(), -touch.dy());
            if (touch.firstTouch()) {
                Action moveUnit = Action.getNullAction();
                Cell c = camera.getCell(world.map, touch.x, touch.y);
                if (way != null) {
                    if (way.isInto(c)) {
                        moveUnit = way.getMoveTo(c);
                    }
                    way = null;
                }
                features.clear();
                if (c.hasUnit()) {
                    features.add(new UnitSelection(c.getUnit()));
                    way = new AlternativeWay(world.map, c.getUnit());
                    features.add(way);
                }
                moveUnit.apply();
            }
        } else {
            camera.move(-(touch.dx() + touch.next.dx()) / 2, -(touch.dy() + touch.next.dy()) / 2);
            Touch t2 = touch.next;
            float scale = (float) Math.sqrt(len2(touch.x - t2.x, touch.y - t2.y) / len2(touch.oldX() - t2.oldX(), touch.oldY() - t2.oldY()));
            camera.scale(scale, (touch.x + t2.x) / 2, (touch.y + t2.y) / 2);
        }
    }

    /**
     * отменили действие, сбрасываем состояние
     */
    public void cancelAction() {
        way = null;
        features.clear();
    }

    @Override
    public void startNextTurn() {
        super.startNextTurn();      //обязательно вызывать, там обновляются города
        cancelAction();
        nextTurnReady = false;
    }

    @Override
    public void beforeEndTurn() {
        super.beforeEndTurn();     //обязательно вызывать, обновляются очки движения юнитов
    }

    /**
     * установить изве, что игроку пора закончить ход
     */
    public void setNextTurnReady() {
        nextTurnReady = true;
    }

    private float len2(float x, float y) {
        return (x * x + y * y);
    }
}
