package game.main.gamelogic;

import game.main.GUI.MapCamera;
import game.main.GUI.UnitSelection;
import game.main.GUI.iRenderFeature;
import game.main.gamelogic.meta.Player;
import game.main.input.Touch;

import java.util.List;

/**
 * Created by lgor on 09.02.14.
 * Класс игрока, который сидит и тыкает в экран пальцем.
 */
public class Gamer extends Player {

    public Gamer(World world, int id) {
        super(world, id);
    }

    @Override
    public boolean update(MapCamera camera, Touch[] touches, List<iRenderFeature> features) {
        if (touches != null) {
            for (Touch t : touches) {
                update(camera, t, features);
            }
        }
        return true;
    }

    private void update(MapCamera camera, Touch touch, List<iRenderFeature> features) {
        if (touch.count() == 1) {
            camera.move(-touch.dx(), -touch.dy());
            if (touch.firstTouch()) {
                Cell c = camera.getCell(world.map, touch.x, touch.y);
                features.clear();
                if (c.hasUnit()) {
                    features.add(new UnitSelection(c.getUnit()));
                }
            }
        } else {
            camera.move(-(touch.dx() + touch.next.dx()) / 2, -(touch.dy() + touch.next.dy()) / 2);
            Touch t2 = touch.next;
            float scale = (float) Math.sqrt(len2(touch.x - t2.x, touch.y - t2.y) / len2(touch.oldX() - t2.oldX(), touch.oldY() - t2.oldY()));
            camera.scale(scale, (touch.x + t2.x) / 2, (touch.y + t2.y) / 2);
        }
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
