package game.main.gamelogic;

import game.main.GUI.MapCamera;
import game.main.GUI.UnitSelection;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.world.Action;
import game.main.gamelogic.world.Cell;
import game.main.gamelogic.world.Player;
import game.main.gamelogic.world.World;
import game.main.utils.Touch;
import game.main.utils.Way;

import java.util.List;

/**
 * Created by lgor on 09.02.14.
 * Класс игрока, который сидит и тыкает в экран пальцем.
 */
public class Gamer extends Player {

    private final List<iRenderFeature> features;
    private Way way = null;

    public Gamer(World world, int id, List<iRenderFeature> features) {
        super(world, id);
        this.features = features;
    }

    @Override
    public boolean update(MapCamera camera, List<Touch> touches) {
        for (Touch t : touches) {
            update(camera, t);
        }
        return true;
    }

    private void update(MapCamera camera, Touch touch) {
        if (touch.count() == 1) {
            camera.move(-touch.dx(), -touch.dy());
            if (touch.firstTouch()) {
                Cell c = camera.getCell(world.map, touch.x, touch.y);
                if (way != null) {
                    if (way.isInto(c)) {
                        Action action = way.getMoveTo(c);
                        action.apply();
                    }
                    way = null;
                }
                features.clear();
                if (c.hasUnit()) {
                    features.add(new UnitSelection(c.getUnit()));
                    way = new Way(world.map, c.getUnit());
                    features.add(way);
                }
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
    public void nextStep() {
        super.nextStep();   //обязательно вызывать, там обновляются города
    }

    @Override
    public void theEnd() {
        super.theEnd();     //обязательно вызывать, обновляются очки движения юнитов
    }

    private float len2(float x, float y) {
        return (x * x + y * y);
    }
}
