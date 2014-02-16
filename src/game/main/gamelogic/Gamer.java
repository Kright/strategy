package game.main.gamelogic;

import game.main.GUI.MapCamera;
import game.main.gamelogic.meta.Player;
import game.main.input.Touch;

/**
 * Created by lgor on 09.02.14.
 * Класс игрока, который сидит и тыкает в экран пальцем.
 */
public class Gamer extends Player {

    public Gamer(World world, int id) {
        super(world, id);
    }

    @Override
    public boolean update(MapCamera camera, Touch[] touches) {
        if (touches != null) {
            for (Touch t : touches) {
                update(camera, t);
            }
        }
        return true;
    }

    private void update(MapCamera camera, Touch touch) {
        if (touch.count() == 1) {
            camera.move(-touch.dx(), -touch.dy());
        } else {
            camera.move(-(touch.dx() + touch.next.dx()) / 2, -(touch.dy() + touch.next.dy()) / 2);
            Touch t2 = touch.next;
            float scale = (float) Math.sqrt(len2(touch.x - t2.x, touch.y - t2.y) / len2(touch.oldX() - t2.oldX(), touch.oldY() - t2.oldY()));
            camera.scale(scale, (touch.x + t2.x) / 2, (touch.y + t2.y) / 2);
        }
    }

    @Override
    public void nextStep() {
        super.nextStep();   //обязательно вызывать, там обновляются списки юнитов
    }

    private float len2(float x, float y) {
        return (x * x + y * y);
    }
}
